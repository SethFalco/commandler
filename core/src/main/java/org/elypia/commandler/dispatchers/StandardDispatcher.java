/*
 * Copyright 2019-2020 Elypia CIC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.elypia.commandler.dispatchers;

import org.elypia.commandler.*;
import org.elypia.commandler.api.*;
import org.elypia.commandler.config.*;
import org.elypia.commandler.event.*;
import org.elypia.commandler.exceptions.misuse.*;
import org.elypia.commandler.metadata.*;
import org.elypia.commandler.utils.ChatUtils;
import org.slf4j.*;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.*;
import java.util.regex.*;

/**
 * The default implementation of the StandardDispatcher, this implementation
 * heavily relies on regular expression to match content and map the tokens
 * to command input. This assumes a standard formatted command:<br>
 * <code>{prefix}{module} {command} {params}</code>
 * with exceptional circumstances such as default or static commands.
 *
 * @author seth@elypia.org (Seth Falco)
 */
@ApplicationScoped
public class StandardDispatcher implements Dispatcher {

    /** SLF4J Logger */
    private static final Logger logger = LoggerFactory.getLogger(StandardDispatcher.class);

    /**
     * This matches every argument in the commands,
     * any comma seperated <strong>args</strong> will be
     * split by {@link #itemsPattern} as a list.
     */
    private static final Pattern paramsPattern = Pattern.compile("(?:(?:\"(?:\\\\\"|[^\"])*\"|[^\\s,]+)(?:\\s*,\\s*)?)+");

    /**
     * The item regex, this matches every item within a list of parameters.
     * This is for list parameters as a single parameter can contain multiple items.
     */
    private static final Pattern itemsPattern = Pattern.compile("(?<!\\\\)\"(?<quote>.*?)(?<!\\\\)\"|(?<word>[^\\s]+(?<!,))");

    /** The main {@link Commandler} configuration; this contains all metadata on commands. */
    private final ConfigService configService;

    /** The configuration for the main metadata for controllers and commands. */
    private final ControllerConfig controllerConfig;

    /**
     * @param configService The configuration service which has all Commandler configuration.
     * @throws NullPointerException If the configuration provided is null.
     */
    @Inject
    public StandardDispatcher(final ConfigService configService, final ControllerConfig controllerConfig) {
        this.configService = Objects.requireNonNull(configService);
        this.controllerConfig = Objects.requireNonNull(controllerConfig);
    }

    @Override
    public <S, M> boolean isValid(Request<S, M> request) {
        List<String> prefixes = getPrefixes(request);

        if (prefixes == null)
            return true;

        for (String prefix : prefixes) {
            if (request.getContent().startsWith(prefix))
                return true;
        }

        return false;
    }

    /**
     * Map the command against Commandler using the StandardDispatcher.
     * The StandardDispatcher uses the `org.elypia.commandler.dispatchers.StandardDispatcher.aliases`
     * property to determine if a {@link MetaController} and {@link MetaCommand} support this
     * dispatcher, both the parent {@link MetaController} and {@link MetaCommand} must
     * have this set in order to be usable.
     *
     * @param request The request received by the {@link Integration}.
     * @param <S> The type of source event from the integration.
     * @param <M> The type of message that was received.
     * @return An ActionEvent all event data parsed in a way Commandler is happy to proceed with the request.
     */
    @Override
    public <S, M> ActionEvent<S, M> parse(Request<S, M> request) {
        String prefix = parsePrefix(request);
        String content = request.getContent();

        if (prefix != null)
            content = content.substring(prefix.length()).trim();

        String[] command = ChatUtils.splitSpaces(content);

        if (command.length == 0)
            throw new OnlyPrefixException("This message only contained the prefix, but no other content.");

        String arg1 = command[0];
        MetaController selectedMetaController = null;
        MetaCommand selectedMetaCommand = null;
        String params = null;

        for (MetaController metaController : controllerConfig.getControllers()) {
            String controllerAliases = metaController.getProperty(this.getClass(), "aliases");

            if (controllerAliases == null)
                continue;

            boolean controllerMatch = controllerAliases.equalsIgnoreCase(arg1.toLowerCase());

            if (controllerMatch) {
                selectedMetaController = metaController;

                if (command.length > 1) {
                    for (MetaCommand metaCommand : metaController.getMetaCommands()) {
                        String controlAliases = metaCommand.getProperty(this.getClass(), "aliases");

                        if (controlAliases == null)
                            continue;

                        boolean controlMatch = controlAliases.equalsIgnoreCase(command[1].toLowerCase());

                        if (controlMatch) {
                            selectedMetaCommand = metaCommand;
                            params = content
                                .replaceFirst("\\Q" + command[0] + "\\E", "")
                                .replaceFirst("\\Q" + command[1] + "\\E", "")
                                .trim();
                            break;
                        }
                    }

                    if (selectedMetaCommand == null) {
                        selectedMetaCommand = metaController.getDefaultControl();

                        if (selectedMetaCommand == null)
                            throw new NoDefaultCommandException(selectedMetaController);
                        else
                            params = content.replaceFirst("\\Q" + command[0] + "\\E", "").trim();
                    }
                } else {
                    MetaCommand data = metaController.getDefaultControl();

                    if (data != null)
                        selectedMetaCommand = data;
                    else
                        throw new NoDefaultCommandException(metaController);

                    params = content
                        .replaceFirst("\\Q" + command[0] + "\\E", "")
                        .trim();
                }

                break;
            }

            for (MetaCommand metaCommand : metaController.getStaticCommands()) {
                String controlAliases = metaCommand.getProperty(this.getClass(), "aliases");

                if (controlAliases == null)
                    continue;

                boolean controlMatch = controlAliases.equalsIgnoreCase(command[0].toLowerCase());

                if (controlMatch) {
                    selectedMetaController = metaController;
                    selectedMetaCommand = metaCommand;

                    params = content
                        .replaceFirst("\\Q" + command[0] + "\\E", "")
                        .trim();

                    break;
                }
            }
        }

        if (selectedMetaController == null)
            throw new ModuleNotFoundException();

        List<List<String>> parameters = parseParameters(params);
        Serializable id = request.getIntegration().getActionId(request.getSource());

        if (id == null)
            throw new IllegalStateException("All user interactions must be associated with a serializable ID.");

        Action action = new Action(id, request.getContent(), selectedMetaController.getName(), selectedMetaCommand.getName(), parameters);
        ActionEvent<S, M> e = new ActionEvent<>(request, action, selectedMetaController, selectedMetaCommand);

        if (!selectedMetaCommand.isValidParamCount(parameters.size()))
            throw new ParamCountMismatchException(e);

        return e;
    }

    private static final Pattern VAR_PATTERN = Pattern.compile("(?i)^\\$\\{(?<KEY>[A-Z\\d_-]+)(?:\\:(?<DEFAULT>.*))?\\}$");

    /**
     * TODO: We shouldn't pollute this class with configuration code.
     *
     * @param request The action request containiner all request info and headers.
     * @return The list of prefixes valid for this {@link Request}, or null
     * if no prefixes are configured.
     */
    private List<String> getPrefixes(Request<?, ?> request) {
        List<String> prefixConfig = configService.getList(String.class, "commandler.dispatcher.prefix");

        if (prefixConfig == null || prefixConfig.isEmpty())
            return null;

        List<String> prefixes = new ArrayList<>();

        for (String config : prefixConfig) {
            Matcher matcher = VAR_PATTERN.matcher(config);

            if (matcher.find()) {
                String key = matcher.group("KEY");
                String value = request.getHeaders().get(key);

                if (value == null || value.isBlank()) {
                    String defaultValue = matcher.group("DEFAULT");

                    if (defaultValue != null)
                        prefixes.add(defaultValue);
                }
                else
                    prefixes.add(value);
            } else {
                prefixes.add(config);
            }
        }

        return prefixes;
    }

    /**
     * @param request The action request containiner all request info and headers.
     * @return The prefix that was used, or null if no prefix was used.
     */
    private <S, M> String parsePrefix(Request<S, M> request) {
        List<String> prefixes = getPrefixes(request);

        if (prefixes != null) {
            Optional<String> optPrefix = prefixes.stream()
                .filter(request.getContent()::startsWith)
                .findAny();

            return optPrefix.orElseThrow(
                () -> new IllegalStateException("Do not call the #parse method if the command is invalid.")
            );
        }

        return null;
    }

    /**
     * @param paramsString The string of parameters provided by the ser.
     * @return A list of list of strings that represent all params and items.
     */
    private List<List<String>> parseParameters(String paramsString) {
        List<List<String>> params = new ArrayList<>();

        if (paramsString.isBlank())
            return params;

        Matcher paramMatcher = paramsPattern.matcher(paramsString);

        while (paramMatcher.find()) {
            List<String> list = new ArrayList<>();

            String group = paramMatcher.group();
            Matcher splitMatcher = itemsPattern.matcher(group);

            while (splitMatcher.find()) {
                String quote = splitMatcher.group("quote");
                list.add((quote != null) ? quote : splitMatcher.group("word"));
            }

            params.add(list);
        }

        return params;
    }
}

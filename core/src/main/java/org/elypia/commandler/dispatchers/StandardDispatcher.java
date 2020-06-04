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
import org.elypia.commandler.annotation.stereotypes.MessageDispatcher;
import org.elypia.commandler.api.*;
import org.elypia.commandler.event.*;
import org.elypia.commandler.exceptions.misuse.*;
import org.elypia.commandler.i18n.CommandlerMessageResolver;
import org.elypia.commandler.metadata.*;
import org.slf4j.*;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.*;
import java.util.regex.*;
import java.util.stream.Collectors;

/**
 * The default implementation of the StandardDispatcher, this implementation
 * heavily relies on regular expression to match content and map the tokens
 * to command input. This assumes a standard formatted command:<br>
 * <code>{prefix}{module} {command} {params}</code>
 * with exceptional circumstances such as default or static commands.
 *
 * @author seth@elypia.org (Seth Falco)
 */
@MessageDispatcher
public class StandardDispatcher implements Dispatcher {

    /** SLF4J Logger */
    private static final Logger logger = LoggerFactory.getLogger(StandardDispatcher.class);

    /** Variable for handling key/value pairs with default value. ${key:default-value} */
    private static final Pattern VAR_PATTERN = Pattern.compile("(?i)^\\$\\{(?<KEY>[A-Z\\d_-]+)(?::(?<DEFAULT>.*))?}$");

    private final StandardDispatcherParameterParser parameterParser;

    /** The main {@link Commandler} configuration; this contains all metadata on commands. */
    private final StandardDispatcherConfig standardDispatcherConfig;

    /** The configuration for the main metadata for controllers and commands. */
    private final CommandlerExtension commandlerExtension;

    /** Get localized strings for prefixes and aliases. */
    private final CommandlerMessageResolver messageResolver;

    /**
     * @param parameterParser Controls how parameters are parsed by this dispatcher.
     * @param standardDispatcherConfig The configuration service which has all Commandler configuration.
     * @param commandlerExtension The configuration for all of the registered controllers in this instance.
     * @throws NullPointerException If the configuration provided is null.
     */
    @Inject
    public StandardDispatcher(StandardDispatcherParameterParser parameterParser, StandardDispatcherConfig standardDispatcherConfig, CommandlerExtension commandlerExtension, CommandlerMessageResolver messageResolver) {
        this.parameterParser = Objects.requireNonNull(parameterParser);
        this.standardDispatcherConfig = Objects.requireNonNull(standardDispatcherConfig);
        this.commandlerExtension = Objects.requireNonNull(commandlerExtension);
        this.messageResolver = Objects.requireNonNull(messageResolver);
    }

    @Override
    public <S, M> boolean isValid(Request<S, M> request) {
        List<String> prefixes = getPrefixes(request);

        if (prefixes.isEmpty())
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

        Pattern delimeter = standardDispatcherConfig.getDelimeter();
        String[] command = delimeter.split(content, 3);

        if (command.length == 0)
            throw new OnlyPrefixException("This message only contained the prefix, but no other content.");

        String arg1 = command[0];
        MetaController selectedMetaController = null;
        MetaCommand selectedMetaCommand = null;
        String params = null;

        for (MetaController metaController : commandlerExtension.getMetaControllers()) {
            String controllerAliases = messageResolver.getMessage(metaController.getProperty(this.getClass(), "aliases"));

            if (controllerAliases == null)
                continue;

            boolean controllerMatch = controllerAliases.equalsIgnoreCase(arg1.toLowerCase());

            if (controllerMatch) {
                selectedMetaController = metaController;

                if (command.length > 1) {
                    for (MetaCommand metaCommand : metaController.getMetaCommands()) {
                        String controlAliases = messageResolver.getMessage(metaCommand.getProperty(this.getClass(), "aliases"));

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
                        selectedMetaCommand = getDefaultCommand(metaController);

                        if (selectedMetaCommand == null)
                            throw new NoDefaultCommandException(selectedMetaController);
                        else
                            params = content.replaceFirst("\\Q" + command[0] + "\\E", "").trim();
                    }
                } else {
                    MetaCommand data = getDefaultCommand(metaController);

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

            for (MetaCommand metaCommand : getStaticCommands(metaController)) {
                String controlAliases = messageResolver.getMessage(metaCommand.getProperty(this.getClass(), "aliases"));

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

        List<List<String>> parameters = parameterParser.parse(params);
        Serializable id = request.getIntegration().getActionId(request.getSource());

        if (id == null)
            throw new IllegalStateException("All user interactions must be associated with a serializable ID.");

        Action action = new Action(id, request.getContent(), selectedMetaController.getControllerType(), selectedMetaCommand.getMethod().getName(), parameters);
        ActionEvent<S, M> e = new ActionEvent<>(request, action, selectedMetaController, selectedMetaCommand);

        if (!selectedMetaCommand.isValidParamCount(parameters.size()))
            throw new ParamCountMismatchException(e);

        return e;
    }

    /**
     * @return All commands in this module where the
     * {@link MetaCommand} is considered static.
     */
    public List<MetaCommand> getStaticCommands(MetaController controller) {
        return controller.getMetaCommands().stream()
            .filter((command) -> command.getProperty(StandardDispatcher.class, "static").equals("true"))
            .collect(Collectors.toUnmodifiableList());
    }

    /**
     * @return Return the default command, or null
     * if this module doesn't have one.
     */
    private MetaCommand getDefaultCommand(MetaController controller) {
        return controller.getMetaCommands().stream()
            .filter((command) -> command.getProperty(StandardDispatcher.class, "default").equals("true"))
            .findAny().orElse(null);
    }

    /**
     * @param request The action request containiner all request info and headers.
     * @return The list of prefixes valid for this {@link Request}, or null
     * if no prefixes are configured.
     */
    private List<String> getPrefixes(Request<?, ?> request) {
        List<String> prefixConfig = standardDispatcherConfig.getPrefixes();

        List<String> prefixes = new ArrayList<>();

        if (prefixConfig == null || prefixConfig.isEmpty())
            return prefixes;

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

        if (!prefixes.isEmpty()) {
            Optional<String> optPrefix = prefixes.stream()
                .filter(request.getContent()::startsWith)
                .findAny();

            return optPrefix.orElseThrow(
                () -> new IllegalStateException("Do not call the #parse method if the command is invalid.")
            );
        }

        return null;
    }
}

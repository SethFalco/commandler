/*
 * Copyright 2019-2019 Elypia CIC
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
import org.elypia.commandler.config.CommandlerConfig;
import org.elypia.commandler.event.*;
import org.elypia.commandler.exceptions.misuse.*;
import org.elypia.commandler.metadata.*;
import org.elypia.commandler.utils.ChatUtils;
import org.slf4j.*;

import javax.inject.*;
import java.io.Serializable;
import java.util.*;
import java.util.regex.*;
import java.util.stream.Stream;

/**
 * The default implementation of the StandardDispatcher, this implementation
 * heavily relies on regular expression to match content and map the tokens
 * to command input. This assumes a standard formatted command:<br>
 * <code>{prefix}{module} {command} {params}</code>
 * with exceptional circumstances such as default or static commands.
 *
 * @author seth@elypia.org (Seth Falco)
 */
@Singleton
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
    private static final Pattern itemsPattern = Pattern.compile("(?<!\\\\)\"(?<quote>.+?)(?<!\\\\)\"|(?<word>[^\\s]+(?<!,))");

    private final AppContext appContext;

    private final CommandlerConfig commandlerConfig;

    /**
     * Create an instance of the StandardDispatcher with no prefix.
     *
     * @param appContext The {@link Commandler} {@link AppContext}.
     */
    @Inject
    public StandardDispatcher(final AppContext appContext, final CommandlerConfig commandlerConfig) {
        this.appContext = Objects.requireNonNull(appContext);
        this.commandlerConfig = Objects.requireNonNull(commandlerConfig);
    }

    @Override
    public boolean isValid(Object source, String content) {
        String[] prefixes = null;

        if (prefixes == null)
            return true;

        for (String prefix : prefixes) {
            if (content.startsWith(prefix))
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
     * @param integration The integration that received the message.
     * @param source The source event that triggered this.
     * @param content The content of the meessage.
     * @param <S> The type of source event from the integration.
     * @param <M> The type of message that was received.
     * @return An ActionEvent all event data parsed in a way Commandler is happy to proceed with the request.
     */
    @Override
    public <S, M> ActionEvent<S, M> parse(Integration<S, M> integration, S source, M message, String content) {
        String input = parsePrefix(content);
        String[] command = ChatUtils.splitSpaces(input);

        if (command.length == 0)
            throw new OnlyPrefixException("This message only contained the prefix, but no other content.");

        String arg1 = command[0];
        MetaController selectedMetaController = null;
        MetaCommand selectedMetaCommand = null;
        String params = null;

        for (MetaController metaController : commandlerConfig.getControllers()) {
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
                            params = input
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
                            params = input.replaceFirst("\\Q" + command[0] + "\\E", "").trim();
                    }
                } else {
                    MetaCommand data = metaController.getDefaultControl();

                    if (data != null)
                        selectedMetaCommand = data;
                    else
                        throw new NoDefaultCommandException(metaController);

                    params = input
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

                    params = input
                        .replaceFirst("\\Q" + command[0] + "\\E", "")
                        .trim();

                    break;
                }
            }
        }

        if (selectedMetaController == null)
            throw new ModuleNotFoundException();

        List<List<String>> parameters = parseParameters(params);
        Serializable id = integration.getActionId(source);

        if (id == null)
            throw new IllegalStateException("All user interactions must be associated with a serializable ID.");

        Action action = new Action(id, content, selectedMetaController.getName(), selectedMetaCommand.getName(), parameters);
        ActionEvent<S, M> e = new ActionEvent<>(integration, selectedMetaController, selectedMetaCommand, source, message, action);

        if (!selectedMetaCommand.isValidParamCount(parameters.size()))
            throw new ParamCountMismatchException(e);

        return e;
    }

    /**
     * @param content The command the user sent it.
     * @return The input without the prefix.
     */
    private String parsePrefix(String content) {
        String[] prefixes = null;

        if (prefixes != null) {
            Optional<String> optPrefix = Stream.of(prefixes)
                .filter(content::startsWith)
                .findAny();

            String prefix = optPrefix.orElseThrow(
                () -> new IllegalStateException("Do not call the #parse method if the command is invalid.")
            );

            content = content.substring(prefix.length());
        }

        return content;
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

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
import org.elypia.commandler.exceptions.*;
import org.elypia.commandler.metadata.*;
import org.elypia.commandler.utils.ChatUtils;
import org.slf4j.*;

import javax.inject.*;
import java.io.Serializable;
import java.util.*;
import java.util.function.Function;
import java.util.regex.*;
import java.util.stream.Stream;

/**
 * The default implementation of the StandardDispatcher, this implementation
 * heavily relies on regular expression to match content and map the tokens
 * to command input. This assumes a standard formatted command:<br>
 * <code>{prefix}{module} {command} {params}</code>
 * With exceptional circumstances.
 *
 * @author seth@elypia.org (Syed Shah)
 */
@Singleton
public class StandardDispatcher implements Dispatcher {

    /** SLF4J Logger */
    private static final Logger logger = LoggerFactory.getLogger(StandardDispatcher.class);

    /**
     * This matches every argument in the commands,
     * any comma seperated <strong>args</strong> will be split by {@link #itemsPattern} as a list.
     */
    private static final Pattern paramsPattern = Pattern.compile("(?:(?:\"(?:\\\\\"|[^\"])*\"|[^\\s,]+)(?:\\s*,\\s*)?)+");

    /**
     * The default item regex, this matches every item within a list of parameters.
     * This is for list parameters as a single parameter can contain multiple items.
     */
    private static final Pattern itemsPattern = Pattern.compile("(?<!\\\\)\"(?<quote>.+?)(?<!\\\\)\"|(?<word>[^\\s,]+)");

    private final AppContext appContext;
    private final Function<Object, String[]> prefixProvider;

    /**
     * Create an instance of the StandardDispatcher with no prefix.
     *
     * @param appContext The {@link Commandler} {@link AppContext}.
     */
    @Inject
    public StandardDispatcher(AppContext appContext) {
        this(appContext, (String[])null);
    }

    public StandardDispatcher(AppContext appContext, String... prefixes) {
        this(appContext, (object) -> prefixes);
    }

    public StandardDispatcher(AppContext appContext, Function<Object, String[]> prefixProvider) {
        this.appContext = Objects.requireNonNull(appContext);
        this.prefixProvider = Objects.requireNonNull(prefixProvider);
    }

    @Override
    public boolean isValid(Object source, String content) {
        String[] prefixes = prefixProvider.apply(source);

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
     * @param integration
     * @param source
     * @param content The content of the meessage.
     * @param <M>
     * @return
     */
    @Override
    public <S, M> ActionEvent<S, M> parse(Integration<S, M> integration, S source, String content) {
        String[] prefixes = prefixProvider.apply(source);

        if (prefixes != null) {
            Optional<String> optPrefix = Stream.of(prefixes)
                .filter(content::startsWith)
                .findAny();

            String prefix = optPrefix.orElseThrow(
                () -> new IllegalStateException("Do not call the #parse method if the command is invalid.")
            );

            content = content.substring(prefix.length());
        }

        String[] command = ChatUtils.splitSpaces(content);

        if (command.length == 0)
            throw new OnlyPrefixException("This message only contained the prefix, but no other content.");

        String arg1 = command[0];
        MetaController selectedMetaController = null;
        MetaCommand selectedMetaCommand = null;
        String params = null;

        for (MetaController metaController : appContext.getInjector().getInstance(CommandlerConfig.class).getControllers()) {
            String controllerAliases = metaController.getProperty(this.getClass(), "aliases");

            // TODO: Currently this would mean static commadns can't exist unless the parent module has an alias.
            if (controllerAliases == null)
                continue;

            boolean controllerMatch = controllerAliases.contains(arg1.toLowerCase());

            if (controllerMatch) {
                selectedMetaController = metaController;

                if (command.length > 1) {
                    for (MetaCommand metaCommand : metaController.getMetaCommands()) {
                        String controlAliases = metaCommand.getProperty(this.getClass(), "aliases");

                        if (controlAliases == null)
                            continue;

                        boolean controlMatch = controlAliases.contains(command[1].toLowerCase());

                        if (controlMatch) {
                            selectedMetaCommand = metaCommand;
                            break;
                        }
                    }

                    if (selectedMetaCommand == null)
                        throw new MisuseException("Found module but command doesn't exist."){};

                    params = content
                        .replace(command[0], "")
                        .replace(command[1], "")
                        .trim();
                } else {
                    MetaCommand data = metaController.getDefaultControl();

                    if (data != null)
                        selectedMetaCommand = data;
                    else
                        throw new NoDefaultCommandException(metaController);

                    params = content
                        .replace(command[0], "")
                        .trim();
                }

                break;
            }

            for (MetaCommand metaCommand : metaController.getStaticCommands()) {
                String controlAliases = metaCommand.getProperty(this.getClass(), "aliases");

                if (controlAliases == null)
                    continue;

                boolean controlMatch = controlAliases.contains(command[0].toLowerCase());

                if (controlMatch) {
                    selectedMetaController = metaController;
                    selectedMetaCommand = metaCommand;

                    params = content
                        .replace(command[0], "")
                        .trim();

                    break;
                }
            }
        }

        if (selectedMetaController == null)
            throw new ModuleNotFoundException();

        List<List<String>> parameters = new ArrayList<>();

        if (!params.isBlank()) {
            Matcher paramMatcher = paramsPattern.matcher(params);

            while (paramMatcher.find()) {
                String group = paramMatcher.group();
                Matcher splitMatcher = itemsPattern.matcher(group);

                List<String> list = new ArrayList<>();
                while (splitMatcher.find()) {
                    String quote = splitMatcher.group("quote");
                    list.add(quote != null ? quote : splitMatcher.group("word"));
                }

                parameters.add(list);
            }
        }

        Serializable id = integration.getActionId(source);

        if (id == null)
            throw new IllegalStateException("All user interactions must be associated with a serializable ID.");

        Action action = new Action(id, content, selectedMetaController.getName(), selectedMetaCommand.getName(), parameters);
        ActionEvent<S, M> e = new ActionEvent<>(integration, selectedMetaController, selectedMetaCommand, source, action);

        if (!selectedMetaCommand.isValidParamCount(parameters.size()))
            throw new ParamCountMismatchException(e);

        return e;
    }
}

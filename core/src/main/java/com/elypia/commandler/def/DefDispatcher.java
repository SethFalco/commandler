package com.elypia.commandler.def;

import com.elypia.commandler.*;
import com.elypia.commandler.core.Context;
import com.elypia.commandler.exceptions.misuse.*;
import com.elypia.commandler.interfaces.*;
import com.elypia.commandler.meta.data.*;
import com.elypia.commandler.utils.CommandlerUtils;

import java.util.*;
import java.util.regex.*;

/**
 * The default implementation of the DefDispatcher, this implementation
 * heavily relies on regular expression to match content and map the tokens
 * to command input. This assumes a standard formatted command:<br>
 * <code>{prefix}{module} {command} {params}</code>
 * With exceptional circumstances.
 *
 * @param <S> The (S)ource event.
 * @param <M> The (M)essage sent a received by the client.
 */
public class DefDispatcher<S, M> implements Dispatcher<S, M> {

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

    private final Commandler commandler;
    private final String[] prefixes;
    private final Context context;

    public DefDispatcher(Commandler commandler, String... prefixes) {
        this.commandler = commandler;
        this.prefixes = prefixes;
        this.context = commandler.getContext();
    }

    @Override
    public M dispatch(S source, String content, boolean send) {
        try {
            Object response;
            CommandlerEvent<S> event = parse(commandler, source, content);

            Input input = event.getInput();

            MetaModule module = input.getModule();
            MetaCommand command = input.getCommand();

            Object[] params = commandler.getParser().adaptEvent(event);

            Handler handler = commandler.getInjector().getInstance(module.getHandlerType());

            commandler.getValidator().validate(event, handler, params);

            if (commandler.getTestRunner().isFailing(handler))
                throw new ModuleDisabledException(input);

            response = command.getMethod().invoke(handler, params);

            if (response == null)
                return null;

            M message;

            if (send)
                message = null;// event.send(response);
            else
                message = commandler.getProvider().provide(event, response);

            return message;
        } catch (Exception ex) {
            commandler.getMisuseHandler().route(ex);
        }

        return null;
    }

    @Override
    public CommandlerEvent<S> parse(Commandler commandler, S source, String content) throws OnlyPrefixException, NoDefaultCommandException, ModuleNotFoundException {
        String prefix = isValid(source, content);

        if (prefix == null)
            throw new IllegalStateException("This is not a command.");

        String offsetContent = content.substring(prefix.length());
        String[] command = CommandlerUtils.splitSpaces(offsetContent, 2);

        if (command.length == 0)
            throw new OnlyPrefixException();

        String first = command[0];
        MetaModule usedModule = null;
        MetaCommand usedCommand = null;
        String params = null;

        for (MetaModule metaModule : context.getModules()) {
            if (metaModule.performed(first)) {
                usedModule = metaModule;

                if (command.length == 2) {
                    for (MetaCommand metaCommand : metaModule.getCommands()) {
                        if (metaCommand.performed(command[1]))
                            usedCommand = metaCommand;
                    }

                    if (usedCommand == null)
                        throw new RuntimeException("Found module but command doesn't exist.");

                    params = content
                        .replace(command[0], "")
                        .replace(command[1], "")
                        .trim();
                } else {
                    MetaCommand data = metaModule.getDefaultCommand();

                    if (data != null)
                        usedCommand = data;
                    else
                        throw new NoDefaultCommandException(metaModule);

                    params = content
                        .replace(command[0], "")
                        .trim();
                }

                break;
            }

            for (MetaCommand metaCommand : metaModule.getStaticCommands()) {
                if (metaCommand.performed(command[0]))
                    usedCommand = metaCommand;

                params = content
                    .replace(command[0], "")
                    .trim();
            }
        }

        if (usedModule == null)
            throw new ModuleNotFoundException();

        List<List<String>> parameters = new ArrayList<>();

        if (params.isBlank()) {
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

        Input input = new Input(content, prefix, usedModule, usedCommand, parameters);
        return new CommandlerEvent(source, input);
    }

    @Override
    public String isValid(S source, String content) {
        for (String prefix : prefixes)
            if (content != null && content.startsWith(prefix))
                return prefix;

        return null;
    }

    public String[] getPrefixes(S event) {
        return prefixes;
    }
}

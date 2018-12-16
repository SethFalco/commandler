package com.elypia.commandler.impl;

import com.elypia.commandler.*;
import com.elypia.commandler.interfaces.*;
import com.elypia.commandler.metadata.*;

import java.util.*;
import java.util.regex.*;

/**
 * The default implementation of the CommandProcessor, this implementation
 * heavily relies on regular expression to match content and map the tokens
 * to command input. This assumes a standard formatted command:<br>
 * <code>{prefix}{module} {command} {params}</code>
 * With exceptional circumstances.
 *
 * @param <S> The (S)ource event.
 * @param <M> The (M)essage sent a received by the client.
 */
public class CommandProcessor<S, M> implements ICommandProcessor<S, M> {

    /**
     * The default commands regex, this matches the commands to see if its
     * valid and returns the matches as groups.
     */
    protected static final String COMMAND_REGEX = "(?i)\\A(?:\\Q%s\\E)(?<module>[A-Z\\d]+)(?:\\s+(?<command>[A-Z\\d]+)(?<list>\\s*,\\s*)?)?(?:\\s+(?<params>.+))?\\Z";

    /**
     * The default params regex, this matches every argument in the commands,
     * any comma seperated <strong>args</strong> will be split by {@link #ITEM_PATTERN} as a list.
     */
    protected static final Pattern PARAM_PATTERN = Pattern.compile("(?:(?:\"(?:\\\\\"|[^\"])*\"|[^\\s,]+)(?:\\s*,\\s*)?)+");

    /**
     * The default item regex, this matches every item within a list of parameters.
     * This is for list parameters as a single parameter can contain multiple items.
     */
    protected static final Pattern ITEM_PATTERN = Pattern.compile("\"(?<quote>(?:\\\\\"|[^\"])*)\"|(?<word>[^\\s,]+)");

    protected Commandler<S, M> commandler;

    protected ModulesContext context;

    protected IMisuseHandler<S, M> misuseHandler;

    public CommandProcessor(Commandler<S, M> commandler) {
        this.commandler = commandler;
        this.context = commandler.getContext();
        this.misuseHandler = commandler.getMisuseHandler();
    }

    @Override
    public M dispatch(S source, String content, boolean send) {
        Object response;
        ICommandEvent<S, M> event = process(commandler, source, content);

        if (!event.isValid())
            response = event.getError();

        else {
            CommandInput input = event.getInput();

            CommandData commandData = event.getInput().getCommandData();

            try {
                Object[] params = commandler.getParser().processEvent(event, commandData);

                Handler<S, M> handler = commandler.getHandler((Class<Handler<S, M>>)commandData.getModuleData().getModuleClass());

                if (params == null || !commandler.getValidator().validate(event, handler, params))
                    response = event.getError();

                else {
                    if (!input.getCommand().equalsIgnoreCase("help") && !handler.isEnabled()) {
                        event.invalidate(misuseHandler.onModuleDisabled(event));
                        response = event.getError();
                    } else {
                        response = commandData.getMethod().invoke(handler, params);
                    }
                }

            } catch (Exception ex) {
                event.invalidate(misuseHandler.onException(ex));
                response = event.getError();
            }
        }

        if (response == null)
            return null;

        M message = commandler.getBuilder().build(event, response);

        if (send)
            event.send(message);

        return message;
    }

    @Override
    public ICommandEvent<S, M> process(Commandler<S, M> commandler, S source, String content) {
        Matcher commandMatcher = matchCommand(source, content);

        if (commandMatcher == null)
            return null;

        String module = commandMatcher.group("module");
        String command = commandMatcher.group("command");
        String params = commandMatcher.group("params");

        List<List<String>> parameters = new ArrayList<>();

        if (params != null) {
            Matcher paramMatcher = PARAM_PATTERN.matcher(params);

            while (paramMatcher.find()) {
                String group = paramMatcher.group();
                Matcher splitMatcher = ITEM_PATTERN.matcher(group);

                List<String> list = new ArrayList<>();
                while (splitMatcher.find()) {
                    String quote = splitMatcher.group("quote");
                    list.add(quote != null ? quote : splitMatcher.group("word"));
                }

                parameters.add(list);
            }
        }

        CommandInput input = new CommandInput(content, module, command, parameters);

        ICommandEvent<S, M> event = new CommandEvent<>(commandler, source, input) {

            @Override
            public <T> M send(T output) {
                return null;
            }

            @Override
            public <T> String send(String key, Map<String, T> params) {
                return null;
            }
        };

        normalize(event, commandMatcher.group("list") != null);
        return event;
    }

    @Override
    public boolean isCommand(S source, String content) {
        return matchCommand(source, content) != null;
    }

    private Matcher matchCommand(S source, String content) {
        StringJoiner joiner = new StringJoiner("|");

        for (String prefix : getPrefixes(source))
            joiner.add(prefix);

        String pattern = String.format(COMMAND_REGEX, joiner.toString());
        Pattern commandPattern = Pattern.compile(pattern);

        Matcher matcher = commandPattern.matcher(content);

        return matcher.matches() ? matcher : null;
    }

    @Override
    public String[] getPrefixes(S event) {
        return new String[] { commandler.getPrefix() };
    }

    public boolean normalize(ICommandEvent<S, M> event, boolean firstList) {
        CommandInput input = event.getInput();

        String module = input.getModule();
        String command = input.getCommand();

        for (ModuleData moduleData : context.getModules()) {
            if (moduleData.performed(module)) {
                input.setModuleData(moduleData);

                if (command != null) {
                    for (CommandData commandData : moduleData.getCommands()) {
                        if (commandData.performed(command))
                            return getDefaultOverload(event, commandData, input);
                    }

                    if (firstList)
                        input.getParameters().get(0).add(0, command);
                    else
                        input.getParameters().add(0, List.of(command));
                }

                CommandData defaultCommand = moduleData.getDefaultCommand();

                if (defaultCommand == null) {
                    event.invalidate(misuseHandler.onDefaultNotFound(event));
                    return false;
                }

                return getDefaultOverload(event, defaultCommand, input);
            }

            for (CommandData commandData : moduleData.getStaticCommands()) {
                if (commandData.performed(module)) {
                    if (command != null)
                        input.getParameters().add(0, List.of(command));

                    if (getDefaultOverload(event, commandData, input)) {
                        input.setModuleData(moduleData);
                        return true;
                    }
                }
            }
        }

        event.invalidate(misuseHandler.onModuleNotFound(input.getContent()));
        return false;
    }

    private boolean getDefaultOverload(ICommandEvent<S, M> event, CommandData command, CommandInput input) {
        CommandData defaultOverload = command.getOverload(input.getParameterCount());

        if (defaultOverload == null) {
            event.invalidate(misuseHandler.onParamCountMismatch(input, command));
            return false;
        }

        input.setCommandData(defaultOverload);
        return true;
    }
}

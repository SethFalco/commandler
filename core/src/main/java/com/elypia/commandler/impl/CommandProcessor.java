package com.elypia.commandler.impl;

import com.elypia.commandler.*;
import com.elypia.commandler.interfaces.*;
import com.elypia.commandler.metadata.*;

import java.util.*;
import java.util.regex.*;

public class CommandProcessor<C, E, M> implements ICommandProcessor<C, E, M> {

    /**
     * The default commands regex, this matches the commands to see if its
     * valid and returns the matches as groups.
     */
    protected static final String COMMAND_REGEX = "(?i)\\A(?:%s)(?<module>[A-Z\\d]+)(?:\\s+(?<command>[A-Z\\d]+))?(?:\\s+(?<params>.+))?\\Z";

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

    protected Commandler<C, E, M> commandler;

    protected ModulesContext context;

    protected LanguageEngine<E> engine;

    protected IMisuseHandler misuseHandler;

    protected String prefix;

    public CommandProcessor(Commandler<C, E, M> commandler) {
        this.commandler = commandler;
        this.context = commandler.getContext();
        this.engine = commandler.getEngine();
        this.misuseHandler = commandler.getMisuseHandler();
        this.prefix = commandler.getPrefix();
    }

    @Override
    public M dispatch(E source, String content, boolean send) {
        CommandEvent<C, E, M> event = process(commandler, source, content);

        if (event == null)
            return null;

        CommandInput input = event.getInput();

        if (!context.getAliases().contains(input.getModule().toLowerCase()))
            return event.send(misuseHandler.onModuleNotFound(content));

        if (!normalize(event, input))
            return event.getError();

        CommandData commandData = event.getInput().getCommandData();

        Object response;

        try {
            Object[] params = commandler.getParser().processEvent(event, commandData);

            Handler<C, E, M> handler = commandler.getHandler(commandData.getModuleData().getModuleClass());

            if (params == null || !commandler.getValidator().validate(event, handler, params))
                return event.getError();

            if (!input.getCommand().equalsIgnoreCase("help") && !handler.isEnabled()) {
                event.invalidate(misuseHandler.onModuleDisabled(event));
                return event.getError();
            }

            response = commandData.getMethod().invoke(handler, params);

            if (response != null && send)
                return event.send(response);
        } catch (Exception ex) {
            event.invalidate(misuseHandler.onException(ex));
            return event.getError();
        }

        return commandler.getBuilder().build(event, response);
    }

    @Override
    public CommandEvent process(Commandler<C, E, M> commandler, E event, String content) {
        StringJoiner joiner = new StringJoiner("|");

        for (String prefix : getPrefixes(event))
            joiner.add("\\Q" + prefix + "\\E");

        String pattern = String.format(COMMAND_REGEX, joiner.toString());
        Pattern commandPattern = Pattern.compile(pattern);

        Matcher matcher = commandPattern.matcher(content);

        if (!matcher.matches())
            return null;

        String module = matcher.group("module");
        String command = matcher.group("command");
        List<List<String>> parameters = new ArrayList<>();

        String params = matcher.group("params");

        if (params != null) {
            matcher = PARAM_PATTERN.matcher(params);

            while (matcher.find()) {
                String group = matcher.group();
                Matcher splitMatcher = ITEM_PATTERN.matcher(group);

                List<String> list = new ArrayList<>();
                while (splitMatcher.find()) {
                    String quote = splitMatcher.group("quote");
                    list.add(quote != null ?  quote : splitMatcher.group("word"));
                }

                parameters.add(list);
            }
        }

        CommandInput input = new CommandInput(module, command, parameters);
        return new CommandEvent<>(commandler, input);
    }

    @Override
    public String[] getPrefixes(E event) {
        return new String[] {prefix};
    }

    public boolean normalize(ICommandEvent<C, E, M> event, CommandInput input) {
        for (ModuleData moduleData : context.getModules()) {
            if (moduleData.performed(input.getModule())) {
                input.setModuleData(moduleData);

                if (input.getCommand() != null) {
                    CommandData commandData = moduleData.getCommand(input.getCommand());

                    if (commandData != null) {
                        CommandData overload = commandData.getOverload(input.getParameterCount());

                        if (overload == null) {
                            event.invalidate(misuseHandler.onParamCountMismatch(input, commandData));
                            return false;
                        }

                        input.setCommandData(overload);
                        return true;
                    }

                    input.getParameters().add(0, List.of(input.getCommand()));
                }

                CommandData defaultCommand = moduleData.getDefaultCommand();

                if (defaultCommand == null) {
                    event.invalidate(misuseHandler.onDefaultNotFound(event));
                    return false;
                }

                CommandData defaultOverload = defaultCommand.getOverload(input.getParameterCount());

                if (defaultOverload == null) {
                    event.invalidate(misuseHandler.onParamCountMismatch(input, defaultCommand));
                    return false;
                }

                input.setCommandData(defaultOverload);
                return true;
            }

            for (CommandData commandData : moduleData.getStaticCommands()) {
                if (commandData.performed(input.getModule())) {
                    if (input.getCommand() != null)
                        input.getParameters().add(0, List.of(input.getCommand()));

                    CommandData defaultOverload = commandData.getOverload(input.getParameterCount());

                    if (defaultOverload == null) {
                        event.invalidate(misuseHandler.onParamCountMismatch(input, commandData));
                        return false;
                    }

                    input.setCommandData(defaultOverload);
                    input.setModuleData(moduleData);
                    return true;
                }
            }
        }

        return false;
    }
}

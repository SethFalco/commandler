package com.elypia.commandler.impl;

import com.elypia.commandler.*;
import com.elypia.commandler.interfaces.*;
import com.elypia.commandler.metadata.CommandData;

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

    protected Commandler commandler;

    protected ModulesContext context;

    protected LanguageEngine<E> engine;

    protected IMisuseHandler handler;

    protected String prefix;

    public CommandProcessor(Commandler<C, E, M> commandler) {
        this.commandler = commandler;
        this.context = commandler.getContext();
        this.engine = commandler.getEngine();
        this.handler = commandler.getMisuseListener();
        this.prefix = commandler.getPrefix();
    }

    @Override
    public M dispatch(E source, String content, boolean send) {
        CommandEvent<C, E, M> event = process(commandler, source, content);

        if (event == null)
            return null;

        CommandInput<C, E, M> input = event.getInput();

        if (!context.getAliases().contains(input.getModule().toLowerCase()))
            return event.send(handler.onModuleNotFound(content));

//        if (!normalize(event, input))
//            return event.getError();

        CommandData commandData = event.getInput().getCommandData();

        Object[] params = commandler.getParser().processEvent(event, commandData);

        if (params == null || !commandler.getCommandValidator().validate(event, params))
            return event.getError();

        if (!input.getCommand().equalsIgnoreCase("help") && !event.getHandler().isEnabled()) {
            event.invalidate(handler.onModuleDisabled(event));
            return event.getError();
        }

        try {
            Object response = commandData.getMethod().invoke(event.getHandler(), params);

            if (response != null && send)
                return event.send(response);
        } catch (Exception ex) {
            event.invalidate(handler.onException(ex));
            return event.getError();
        }

        return null;
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

        CommandInput<C, E, M> input = new CommandInput<>(module, command, parameters);
        return new CommandEvent<>(engine, input);
    }

    @Override
    public String[] getPrefixes(E event) {
        return new String[] {prefix};
    }

//    public boolean normalize(ICommandEvent<C, E, M> event, CommandInput input) {
//        for (ModuleData moduleData : context.getModules()) {
//            if (moduleData.performed(input.getModule())) {
//                input.setModuleData(moduleData);
//
//                if (input.getCommand() != null) {
//                    CommandData commandData = moduleData.getCommand(input.getCommand());
//
//                    if (commandData != null) {
//                        CommandData overload = commandData.getOverload(input.getParameterCount());
//
//                        if (overload == null) {
//                            event.invalidate(commandler.getMisuseListener().onParamCountMismatch(input, commandData));
//                            return false;
//                        }
//
//                        return true;
//                    }
//
//                    input.getParameters().add(0, List.of(input.getCommand()));
//                }
//
//                CommandData defaultCommand = moduleData.getDefaultCommand();
//
//                if (defaultCommand == null) {
//                    event.invalidate(commandler.getMisuseListener().onDefaultNotFound(event));
//                    return false;
//                }
//
//                input.setCommandData(defaultCommand.getOverload(input.getParameterCount()));
//
//                if (input.getCommandData() == null) {
//                    event.invalidate(commandler.getMisuseListener().onParamCountMismatch(this, defaultCommand));
//                    return false;
//                }
//
//                command = commandData.getCommand().aliases()[0];
//                return true;
//            }
//
//            for (CommandData commandData : moduleData.getStaticCommands()) {
//                if (commandData.performed(module)) {
//                    if (command != null)
//                        parameters.add(0, Collections.singletonList(command));
//
//                    this.commandData = commandData.getOverload(getParameterCount());
//
//                    if (this.commandData == null) {
//                        event.invalidate(commandler.getMisuseListener().onParamCountMismatch(this, commandData));
//                        return false;
//                    }
//
//                    this.command = commandData.getCommand().aliases()[0];
//                    this.module = moduleData.getAnnotation().aliases()[0];
//                    this.moduleData = moduleData;
//
//                    return true;
//                }
//            }
//        }
//
//        return false;
//    }
}

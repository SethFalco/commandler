package com.elypia.commandler;

import com.elypia.commandler.annotations.Default;
import com.elypia.commandler.impl.*;
import com.elypia.commandler.interfaces.ICommandProcessor;
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

    protected Commandler commandler;

    @Override
    public M dispatch(E source, String content, boolean send) {
        CommandEvent<C, E, M> event = process(commandler, source, content);

        if (event == null)
            return null;

        CommandInput<C, E, M> input = event.getInput();

        if (!commandler.getRoots().containsKey(input.getModule().toLowerCase()))
            return event.reply(commandler.getMisuseListener().onModuleNotFound(content));

        if (!input.normalize(event))
            return event.getError();

        CommandData commandData = event.getInput().getCommandData();

        if (!commandler.getCommandValidator().validateCommand(event, commandData))
            return event.getError();

        Object[] params = commandler.getParser().processEvent(event, commandData);

        if (params == null || !commandler.getCommandValidator().validateParams(event, params))
            return event.getError();

        if (!input.getCommand().equalsIgnoreCase("help") && !input.getModuleData().getHandler().isEnabled()) {
            event.invalidate(commandler.getMisuseListener().onModuleDisabled(event));
            return event.getError();
        }

        try {
            Object response = commandData.getMethod().invoke(commandData.getHandler(), params);

            if (response != null && send)
                return event.reply(response);
        } catch (Exception ex) {
            event.invalidate(commandler.getMisuseListener().onException(ex));
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

        CommandInput<C, E, M> input = new CommandInput<>(commandler, content, module, command, parameters);

        return new CommandEvent<>(commandler, input, event);
    }

    /**
     * If the command follows valid syntax, normalize the command as
     * depending on if a {@link Default} or {@link Static} command
     * was performed some of the components may need to be shifted
     * or added.
     * <strong>Note:</strong> This may not be required, it depends on the
     * {@link IConfiler#processEvent(Commandler, Object, String)} implementation.
     * The default implementation {@link Confiler} does require normalization. <br>
     *
     * During normalization the following it performed:
     * <ul>
     *     <li>
     *         If the root alias or "module" found is a reference to a module
     *         we select that module. If there is no command that follows, we look
     *         for a default command, else raise an event. If we do find a command
     *         we select that command and perform it.
     *     </li>
     *     <li>
     *         If the root alias or "module" is a reference to a static command
     *         we shift it to "command" and select the module that static command
     *         belongs to, if there is a "command", we insert that as the first
     *         parameter.
     *     </li>
     * </ul>
     *
     * @return If the command is still valid.
     */
    public boolean normalize(ICommandEvent<C, E, M> event) {
        for (IHandler<C, E, M> handler : commandler.getHandlers()) {
            ModuleData moduleData = handler.getModule();

            if (moduleData.performed(module)) {
                this.moduleData = moduleData;

                if (command != null) {
                    CommandData commandData = moduleData.getCommand(command);

                    if (commandData != null) {
                        this.commandData = commandData.getOverload(getParameterCount());

                        if (this.commandData == null) {
                            event.invalidate(commandler.getMisuseListener().onParamCountMismatch(this, commandData));
                            return false;
                        }

                        return true;
                    }

                    // ? If the above hasn't returned, the "command" may actually be a parameter
                    parameters.add(0, Collections.singletonList(command));
                }

                CommandData defaultCommand = moduleData.getDefaultCommand();

                if (defaultCommand == null) {
                    event.invalidate(commandler.getMisuseListener().onDefaultNotFound(event));
                    return false;
                }

                this.commandData = defaultCommand.getOverload(getParameterCount());

                if (this.commandData == null) {
                    event.invalidate(commandler.getMisuseListener().onParamCountMismatch(this, defaultCommand));
                    return false;
                }

                command = commandData.getCommand().aliases()[0];
                return true;
            }

            for (CommandData commandData : moduleData.getStaticCommands()) {
                if (commandData.performed(module)) {
                    if (command != null)
                        parameters.add(0, Collections.singletonList(command));

                    this.commandData = commandData.getOverload(getParameterCount());

                    if (this.commandData == null) {
                        event.invalidate(commandler.getMisuseListener().onParamCountMismatch(this, commandData));
                        return false;
                    }

                    this.command = commandData.getCommand().aliases()[0];
                    this.module = moduleData.getAnnotation().aliases()[0];
                    this.moduleData = moduleData;

                    return true;
                }
            }
        }

        return false;
    }
}

package com.elypia.commandler;

import com.elypia.commandler.annotations.*;
import com.elypia.commandler.impl.*;
import com.elypia.commandler.metadata.*;

import java.util.*;

public class CommandInput<C, E, M> {

    private Commandler<C, E, M> commandler;

    private IConfiler<C, E, M> confiler;

    private String content;

    private ModuleData<C, E, M> moduleData;

    private String module;

    private CommandData<C, E, M> commandData;

    private String command;

    private List<List<String>> parameters;

    public CommandInput(Commandler<C, E, M> commandler, String content, String module, String command, List<List<String>> parameters) {
        this.commandler = Objects.requireNonNull(commandler);
        this.content = Objects.requireNonNull(content);
        this.module = Objects.requireNonNull(module);
        this.command = command;
        this.parameters = Objects.requireNonNull(parameters);

        confiler = commandler.getConfiler();
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
            ModuleData<C, E, M> moduleData = handler.getModule();

            if (moduleData.performed(module)) {
                this.moduleData = moduleData;

                if (command != null) {
                    CommandData<C, E, M> commandData = moduleData.getCommand(command);

                    if (commandData != null) {
                        this.commandData = commandData.getOverload(getParameterCount());

                        if (this.commandData == null) {
                            event.invalidate(confiler.getMisuseListener().onParamCountMismatch(this, commandData));
                            return false;
                        }

                        return true;
                    }

                    // ? If the above hasn't returned, the "command" may actually be a parameter
                    parameters.add(0, Collections.singletonList(command));
                }

                CommandData<C, E, M> defaultCommand = moduleData.getDefaultCommand();

                if (defaultCommand == null) {
                    event.invalidate(confiler.getMisuseListener().onDefaultNotFound(event));
                    return false;
                }

                this.commandData = defaultCommand.getOverload(getParameterCount());

                if (this.commandData == null) {
                    event.invalidate(confiler.getMisuseListener().onParamCountMismatch(this, defaultCommand));
                    return false;
                }

                command = commandData.getCommand().aliases()[0];
                return true;
            }

            for (CommandData<C, E, M> commandData : moduleData.getStaticCommands()) {
                if (commandData.performed(module)) {
                    if (command != null)
                        parameters.add(0, Collections.singletonList(command));

                    this.commandData = commandData.getOverload(getParameterCount());

                    if (this.commandData == null) {
                        event.invalidate(confiler.getMisuseListener().onParamCountMismatch(this, commandData));
                        return false;
                    }

                    this.command = commandData.getCommand().aliases()[0];
                    this.module = moduleData.getModule().aliases()[0];
                    this.moduleData = moduleData;

                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public String toString() {
        if (parameters.isEmpty())
            return "(0) None";

        StringJoiner parameterJoiner = new StringJoiner(", ");

        for (List<String> list : parameters) {
            StringJoiner itemJoiner = new StringJoiner(", ");

            for (String string : list)
                itemJoiner.add("'" + string + "'");

            if (list.size() > 1)
                parameterJoiner.add("[" + itemJoiner.toString() + "]");
            else
                parameterJoiner.add(itemJoiner.toString());
        }

        return "(" + parameters.size() + ") " + parameterJoiner.toString();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ModuleData getModuleData() {
        return moduleData;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public CommandData<C, E, M> getCommandData() {
        return commandData;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public List<List<String>> getParameters() {
        return parameters;
    }

    public void setParameters(List<List<String>> parameters) {
        this.parameters = parameters;
    }

    public int getParameterCount() {
        return parameters.size();
    }
}

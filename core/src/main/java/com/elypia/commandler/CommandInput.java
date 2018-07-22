package com.elypia.commandler;

import com.elypia.commandler.annotations.*;
import com.elypia.commandler.impl.*;
import com.elypia.commandler.metadata.*;

import java.util.*;

public class CommandInput<C, E, M> {

    private Commandler<C, E, M> commandler;

    private IConfiler<C, E, M> confiler;

    private String content;

    private MetaModule<C, E, M> metaModule;

    private String module;

    private MetaCommand<C, E, M> metaCommand;

    private String command;

    private List<List<String>> parameters;

    private int parameterCount;

    public CommandInput(Commandler<C, E, M> commandler, String content, String module, String command, List<List<String>> parameters) {
        this.commandler = Objects.requireNonNull(commandler);
        this.content = Objects.requireNonNull(content);
        this.module = Objects.requireNonNull(module);
        this.command = command;
        this.parameters = Objects.requireNonNull(parameters);

        confiler = commandler.getConfiler();
        parameterCount = parameters.size();
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
    public boolean normalize(CommandEvent<C, E, M> event) {
        for (IHandler<C, E, M> handler : commandler.getHandlers()) {
            MetaModule<C, E, M> metaModule = handler.getModule();

            if (metaModule.performed(module)) {
                this.metaModule = metaModule;

                if (command != null) {
                    MetaCommand<C, E, M> metaCommand = metaModule.getCommand(command);

                    if (metaCommand != null) {
                        this.metaCommand = metaCommand;

                        if (parameterCount != metaCommand.getInputRequired()) {
                            for (MetaCommand<C, E, M> metaOverload : metaCommand.getOverloads()) {
                                if (parameterCount == metaOverload.getInputRequired()) {
                                    this.metaCommand = metaOverload;
                                    return true;
                                }
                            }

                            event.invalidate(confiler.getMisuseListener().onParameterCountMismatch(this, metaCommand));
                            return false;
                        }

                        return true;
                    }
                }

                MetaCommand<C, E, M> defaultCommand = metaModule.getDefaultCommand();

                if (defaultCommand == null) {
                    event.invalidate(confiler.getMisuseListener().onNoDefault(event));
                    return false;
                }

                if (command != null)
                    parameters.add(0, Collections.singletonList(command));

                if (parameters.size() != defaultCommand.getInputRequired()) {
                    for (MetaCommand<C, E, M> metaOverload : defaultCommand.getOverloads()) {
                        if (parameters.size() == metaOverload.getInputRequired()) {
                            this.metaCommand = metaOverload;
                            return true;
                        }
                    }

                    return false;
                }

                this.metaCommand = defaultCommand;
                return true;
            }

            for (MetaCommand metaCommand : metaModule.getStaticCommands()) {
                if (metaCommand.performed(module)) {
                    if (command != null)
                        parameters.add(0, Collections.singletonList(command));

                    this.command = metaModule.getModule().aliases()[0];
                    this.metaModule = metaModule;

                    this.command = metaCommand.getCommand().aliases()[0];
                    this.metaCommand = metaCommand;

                    if (parameters.size() != metaCommand.getInputRequired())
                        return false;

                    return true;
                }
            }
        }

        return false;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public MetaModule getMetaModule() {
        return metaModule;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public MetaCommand<C, E, M> getMetaCommand() {
        return metaCommand;
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
}

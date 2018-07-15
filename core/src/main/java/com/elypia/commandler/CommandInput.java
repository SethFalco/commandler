package com.elypia.commandler;

import com.elypia.commandler.annotations.*;
import com.elypia.commandler.impl.IConfiler;
import com.elypia.commandler.metadata.*;
import com.elypia.commandler.modules.CommandHandler;

import java.util.*;

public class CommandInput {

    private Commandler commandler;

    private IConfiler confiler;

    private String content;

    private MetaModule metaModule;

    private String module;

    private AbstractMetaCommand abstractMetaCommand;

    private String command;

    private List<List<String>> parameters;

    private int parameterCount;

    public CommandInput(Commandler commandler, String content, String module, String command, List<List<String>> parameters) {
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

    public boolean normalize() {
        for (CommandHandler handler : commandler.getHandlers()) {
            MetaModule metaModule = handler.getModule();

            if (metaModule.hasPerformed(module)) {
                this.metaModule = metaModule;

                if (command != null) {
                    MetaCommand metaCommand = metaModule.getCommand(command);

                    if (metaCommand != null) {
                        this.abstractMetaCommand = metaCommand;

                        if (parameterCount != metaCommand.getInputRequired()) {
                            for (MetaOverload metaOverload : metaCommand.getOverloads()) {
                                if (parameterCount == metaOverload.getInputRequired()) {
                                    this.abstractMetaCommand = metaOverload;
                                    return true;
                                }
                            }

                            confiler.getMisuseListener().parameterCountMismatch(this, metaCommand);
                            return false;
                        }

                        return true;
                    }
                }

                MetaCommand defaultCommand = metaModule.getDefaultCommand();

                if (defaultCommand == null)
                    return event.invalidate("You've specified a module without a valid command, however there is no default command associated with this module.");

                if (command != null)
                    event.getParams().add(0, Collections.singletonList(command));

                if (event.getParams().size() != defaultCommand.getInputRequired()) {
                    for (MetaOverload metaOverload : defaultCommand.getOverloads()) {
                        if (event.getParams().size() == metaOverload.getInputRequired()) {
                            event.setMetaCommand(metaOverload);
                            return true;
                        }
                    }

                    return event.invalidate("It seems the command you attemped to do doesn't exist, maybe you should try the help command instead?");
                }

                event.setMetaCommand(defaultCommand);
                return true;
            }

            for (MetaCommand metaCommand : metaModule.getStaticCommands()) {
                if (metaCommand.hasPerformed(module)) {
                    if (command != null)
                        event.getParams().add(0, Collections.singletonList(command));

                    event.setMetaModule(metaModule);
                    event.setMetaCommand(metaCommand);

                    if (event.getParams().size() != metaCommand.getInputRequired())
                        return event.invalidate("The parameters you provided doesn't match up with the paramaters this command required.");

                    return true;
                }
            }
        }

        return event.invalidate(null);
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

    public AbstractMetaCommand getMetaCommand() {
        return abstractMetaCommand;
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

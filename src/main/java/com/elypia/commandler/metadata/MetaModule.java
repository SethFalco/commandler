package com.elypia.commandler.metadata;

import com.elypia.commandler.Commandler;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.exceptions.*;
import com.elypia.commandler.modules.CommandHandler;

import java.lang.reflect.Method;
import java.util.*;

public class MetaModule {

    /**
     * Parent {@link Commandler} object this {@link MetaModule} is registered too.
     */

    private Commandler commandler;

    /**
     * The {@link CommandHandler} this {@link MetaModule} is describing.
     */

    private CommandHandler handler;

    /**
     * The {@link Class} type of the {@link CommandHandler} this {@link MetaModule} is describing.
     */

    private Class<? extends CommandHandler> clazz;

    /**
     * The metadata associated with this module.
     */

    private Module module;

    /**
     * Does this module have a description. If the module doesn't specify a
     * description, it may be hidden from help commands and pages.
     */

    private boolean isPublic;

    /**
     * A list of collected aliases from this module. This is used to compare
     * any user input too as we convert all aliases to lower case in advance.
     */

    private Set<String> aliases;

    /**
     * A list of {@link MetaCommand} that were created inside the {@link CommandHandler}.
     */

    private Collection<MetaCommand> metaCommands;

    /**
     * A list of aliases that belong to commands under this module. This is used to
     * verify we never register two identical command aliases under the same module.
     */

    private Set<String> commandAliases;

    /**
     * Create a wrapper object around this {@link CommandHandler} to obtain any
     * static information on the method itself or the module annotation as well as
     * convinience methods to obtain any validation.
     *
     * @param commandler The parent this {@link CommandHandler} is registered to.
     * @param t The {@link CommandHandler} to obtain information from.
     * @param <T> An instance of the {@link CommandHandler} class.
     * @return A wrapper object around the command handler and it's commands.
     */

    public static <T extends CommandHandler> MetaModule of(Commandler commandler, T t) {
        return new MetaModule(commandler, t);
    }

    /**
     * @see #of(Commandler, CommandHandler)
     *
     * @param commandler The parent this {@link CommandHandler} is registered to.
     * @param t The {@link CommandHandler} to obtain information from.
     * @param <T> An instance of the {@link CommandHandler} class.
     */

    private <T extends CommandHandler> MetaModule(Commandler commandler, T t) {
        handler = t;
        clazz = t.getClass();
        module = clazz.getAnnotation(Module.class);

        if (module == null) {
            String format = "Command handler %s isn't annotated with the Module annotation.";
            throw new MalformedModuleException(String.format(format, clazz.getName()));
        }

        aliases = new HashSet<>();

        for (String alias : module.aliases())
            aliases.add(alias.toLowerCase());

        if (aliases.size() != module.aliases().length) {
            String format = "Module %s (%s) contains multiple aliases which are identical.";
            System.err.printf(format, module.name(), clazz.getName());
        }

        this.commandler = commandler;

        if (!Collections.disjoint(commandler.getRootAlises(), aliases)) {
            String name = module.name();
            String format = "Module %s contains an alias which has already been registered by a previous module or static command.";
            throw new RecursiveAliasException(String.format(format, name));
        }

        commandler.getRootAlises().addAll(aliases);

        Method[] methods = clazz.getDeclaredMethods();
        metaCommands = new ArrayList<>();
        commandAliases = new HashSet<>();

        int defaultCommand = 0;

        for (Method method : methods) {
            if (method.isAnnotationPresent(Command.class)) {
                MetaCommand metaCommand = MetaCommand.of(this, method);

                if (metaCommand.isDefault()) {
                    if (++defaultCommand == 2) {
                        String format = "Module %s (%s) contains multiple default commands, modules may only have a single default.";
                        throw new MalformedModuleException(String.format(format, module.name(), clazz.getName()));
                    }
                }

                commandAliases.addAll(metaCommand.getAliases());
                metaCommands.add(metaCommand);
            }
        }

        isPublic = !module.description().equals("");
    }

    /**
     * @param input The input module by the user.
     * @return If this module contains an entry of that module.
     */

    public boolean hasPerformed(String input) {
        return aliases.contains(input.toLowerCase());
    }

    public MetaCommand getDefaultCommand() {
        for (MetaCommand metaCommand : metaCommands) {
            if (metaCommand.isDefault())
                return metaCommand;
        }

        return null;
    }

    public MetaCommand getCommand(String input) {
        for (MetaCommand metaCommand : metaCommands) {
            if (metaCommand.getAliases().contains(input))
                return metaCommand;
        }

        return null;
    }

    public Commandler getCommandler() {
        return commandler;
    }

    public CommandHandler getHandler() {
        return handler;
    }

    public Class<? extends CommandHandler> getHandlerType() {
        return clazz;
    }

    public Module getModule() {
        return module;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public Collection<String> getAliases() {
        return aliases;
    }

    public Collection<MetaCommand> getMetaCommands() {
        return metaCommands;
    }

    public Collection<String> getCommandAliases() {
        return commandAliases;
    }
}

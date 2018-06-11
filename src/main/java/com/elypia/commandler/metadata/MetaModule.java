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
     * A list of collected aliases from this module. This is used to compare
     * any user input too as we convert all aliases to lower case in advance.
     */

    private Collection<String> aliases;

    /**
     * A list of {@link MetaCommand} that were created inside the {@link CommandHandler}.
     */

    private Collection<MetaCommand> commands;

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
        clazz = t.getClass();
        module = clazz.getAnnotation(Module.class);

        if (module == null) {
            String name = clazz.getName();
            String message = String.format("Command handler %s isn't annotated with the Module annotation.", name);
            throw new MalformedModuleException(message);
        }

        this.commandler = commandler;
        handler = t;

        aliases = new ArrayList<>();

        for (String alias : module.aliases())
            aliases.add(alias.toLowerCase());

        if (!Collections.disjoint(commandler.getRootAlises(), aliases)) {
            String name = module.name();
            String format = "Module %s contains an alias which has already been registered by a previous module or static command.";
            String message = String.format(format, name);
            throw new RecursiveAliasException(message);
        }

        commandler.getRootAlises().addAll(aliases);

        commands = new ArrayList<>();

        Method[] methods = clazz.getDeclaredMethods();

        for (Method method : methods) {
            if (method.isAnnotationPresent(Command.class))
                commands.add(MetaCommand.of(commandler, method));
        }
    }

    /**
     * @param input The input module by the user.
     * @return If this module contains an entry of that module.
     */

    public boolean hasPerformed(String input) {
        return aliases.contains(input.toLowerCase());
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

    public Collection<String> getAliases() {
        return aliases;
    }

    public Collection<MetaCommand> getCommands() {
        return commands;
    }
}

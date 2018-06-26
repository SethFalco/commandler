package com.elypia.commandler.metadata;

import com.elypia.commandler.*;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.exceptions.*;
import com.elypia.commandler.modules.CommandHandler;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class MetaModule implements Comparable<MetaModule> {

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

    private List<MetaCommand> metaCommands;

    /**
     * The {@link Default} command for this module. This value
     * can be null if no default command is specified.
     */

    private MetaCommand defaultCommand;

    /**
     * Create a wrapper object around this {@link CommandHandler} to obtain any
     * static information on the method itself or the module annotation as well as
     * convinience methods to obtain any validation.
     *
     * @param commandler The parent this {@link CommandHandler} is registered to.
     * @param t The {@link CommandHandler} to obtain information from.
     * @param <T> An instance of the {@link CommandHandler} class.
     * @return A wrapper object around the commands handler and it's commands.
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
        this.commandler = Objects.requireNonNull(commandler);
        handler = Objects.requireNonNull(t);
        clazz = t.getClass();
        module = clazz.getAnnotation(Module.class);

        if (module == null)
            throw new MalformedModuleException(String.format("Command handler %s isn't annotated with the Module annotation.", clazz.getName()));

        parseAliases();
        parseMethods();

        isPublic = !module.hidden();
    }

    /**
     * Parse aliases from this module, this creates a list of aliases.
     * All aliases on the module are converted to lower case and added to this.
     * If find duplicates, we continue filter them however warn the user as this
     * shouldn't be the case. <br>
     * Should an alias be registered that was already registed by another module,
     * we throw an exception as this is considered a malformed command.
     * If everything checks out alright, we add all the aliases to the list
     * of {@link Commandler#rootAlises root aliases} and reserve these so other
     * modules of static commands can't try consume them.
     *
     * @throws RecursiveAliasException If this module has an alias which was already registered by
     * another module or static command.
     */

    private void parseAliases() {
        aliases = new HashSet<>();

        for (String alias : module.aliases())
            aliases.add(alias.toLowerCase());

        if (aliases.size() != module.aliases().length)
            Utils.log("Module %s (%s) contains multiple aliases which are identical.", module.name(), clazz.getName());

        if (Collections.disjoint(commandler.getRootAlises(), aliases))
            commandler.getRootAlises().addAll(aliases);
        else
            throw new RecursiveAliasException(String.format("Module %s contains an alias which has already been registered by a previous module or static command.", module.name()));
    }

    /**
     * Parses the methods in this Module and creates {@link MetaCommand} instances
     * out of methods with the {@link Command} annotation. <br>
     * This method ensures only one command in this module is a {@link Default default} command
     * if any, and validates all the underlying Commands that are created before adding their aliases
     * to the global list and utlimatly adding the command as valid. <br>
     * <strong>If <em>ANY</em> commands are invalid, the entire module will fail.</strong>
     *
     * @throws MalformedModuleException When module specified more than one command with the {@link Default} annotation.
     */

    private void parseMethods() {
        metaCommands = new ArrayList<>();

        Set<String> commandAliases = new HashSet<>();
        int defaultCommands = 0;

        Method[] methods = clazz.getMethods();
        methods = Arrays.stream(methods).filter(m -> m.isAnnotationPresent(Command.class)).toArray(Method[]::new);

        for (Method method : methods) {
            MetaCommand metaCommand = MetaCommand.of(this, method);

            if (metaCommand.isDefault()) {
                if (++defaultCommands == 2)
                    throw new MalformedModuleException(String.format("Module %s (%s) contains multiple default commands, modules may only have a single default.", module.name(), clazz.getName()));
            }

            if (Collections.disjoint(commandAliases, metaCommand.getAliases()))
                commandAliases.addAll(metaCommand.getAliases());
            else
                throw new RecursiveAliasException(String.format("Command %s in module %s (%s) contains an alias which has already been registered by a previous command in this module.", metaCommand.getCommand().name(), module.name(), clazz.getName()));

            metaCommands.add(metaCommand);

            if (metaCommand.isDefault())
                defaultCommand = metaCommand;
        }

        Collections.sort(metaCommands);
    }

    /**
     * @param input The input module by the user.
     * @return If this module contains an entry of that module.
     */

    public boolean hasPerformed(String input) {
        return aliases.contains(input.toLowerCase());
    }

    public List<MetaCommand> getPublicCommands() {
        return metaCommands.stream().filter(MetaCommand::isPublic).collect(Collectors.toList());
    }

    /**
     * @return A list of all {@link Static} commands in the module.
     */

    public List<MetaCommand> getStaticCommands() {
        return metaCommands.stream().filter(MetaCommand::isStatic).collect(Collectors.toList());
    }

    public MetaCommand getCommand(String input) {
        input = input.toLowerCase();

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

    public MetaCommand getDefaultCommand() {
        return defaultCommand;
    }

    @Override
    public int compareTo(MetaModule o) {
        return module.name().compareToIgnoreCase(o.module.name());
    }
}

package com.elypia.commandler.metadata;

import com.elypia.commandler.Commandler;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.impl.IHandler;
import org.slf4j.*;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class ModuleData<H extends IHandler> implements Comparable<ModuleData> {

    /**
     * We use SLF4J for logging, be sure to include a binding so you can see
     * warnings and messages.
     */
    private static final Logger logger = LoggerFactory.getLogger(ModuleData.class);

    /**
     * The class this module data belongs too.
     */
    private Class<H> clazz;

    /**
     * The metadata associated with this module.
     */
    private Module module;

    /**
     * A list of collected aliases from this module. This is used to compare
     * any user input too as we convert all aliases to lower case in advance.
     */
    private Set<String> aliases;

    /**
     * Does this module have a description. If the module doesn't specify a
     * description, it may be hidden from help commands and pages.
     */
    private boolean isPublic;

    /**
     * A list of {@link CommandData} that were created inside the {@link IHandler}.
     * This does not include {@link Overload}s, these are stored inside
     * {@link CommandData#getOverloads()}.
     */
    private List<CommandData> commandData;

    /**
     * The {@link Default} command for this module. This value
     * can be null if no default command is specified.
     */
    private CommandData defaultCommand;

    public ModuleData(Class<H> clazz) {
        this.clazz = clazz;
        module = clazz.getAnnotation(Module.class);

        if (module == null) {
            String className = clazz.getName();
            throw new IllegalStateException(String.format("Command handler %s isn't annotated with @Module.", className));
        }

        aliases = new HashSet<>();
        commandData = new ArrayList<>();

        parseAliases();
        parseMethods();

        isPublic = !module.help().equals(Module.DEFAULT_HELP);
    }

    /**
     * Parse aliases from this module, this creates a list of aliases.
     * All aliases on the module are converted to lower case and added to this.
     * If find duplicates, we continue filter them however warn the user as this
     * shouldn't be the case. <br>
     * Should an alias be registered that was already registed by another module,
     * we throw an exception as this is considered a malformed command.
     * If everything checks out alright, we add all the aliases to the list
     * of {@link Commandler#roots root aliases} and reserve these so other
     * modules of static commands can't try consume them.
     *
     * @throws IllegalStateException If this module has an alias which was already registered by
     * another module or static command.
     */
    private void parseAliases() {
        for (String alias : module.aliases()) {
            alias = alias.toLowerCase();

            ModuleData existing = commandler.getRoots().get(alias);

            if (existing != null) {
                String thisModule = module.name();
                String thisClass = handler.getClass().getName();
                String existingModule = existing.module.name();
                String existingClass = existing.handler.getClass().getName();

                throw new IllegalStateException(String.format("Module %s (%s) contains an alias which has already been registered by %s (%s).", thisModule, thisClass, existingModule, existingClass));
            }

            aliases.add(alias.toLowerCase());
        }

        if (aliases.size() != module.aliases().length)
            logger.warn("Module {} ({}) contains multiple aliases that are identical.", module.name(), handler.getClass().getName());

        for (String alias : aliases)
            commandler.getRoots().put(alias, this);
    }

    /**
     * Parses the methods in this Module and creates {@link CommandData} instances
     * out of methods with the {@link Command} annotation. <br>
     * This method ensures only one command in this module is a {@link Default default} command
     * if any, and validates all the underlying Commands that are created before adding their aliases
     * to the global list and utlimatly adding the command as valid. <br>
     * <strong>If <em>ANY</em> commands are invalid, the entire module will fail.</strong>
     *
     * @throws IllegalStateException When module specified more than one command with the {@link Default} annotation.
     */
    private void parseMethods() {
        Set<String> commandAliases = new HashSet<>();
        Method[] methods = handler.getClass().getMethods();
        methods = Arrays.stream(methods).filter(m -> m.isAnnotationPresent(Command.class)).toArray(Method[]::new);

        if (methods.length == 0)
            logger.warn("Module {} ({}) contains no commands.", module.name(), handler.getClass().getName());

        for (Method method : methods) {
            CommandData<C, E, M> commandData = new CommandData<>(this, method);

            if (commandData.isDefault()) {
                if (defaultCommand != null) {
                    String moduleName = module.name();
                    String typeName = handler.getClass().getName();

                    throw new IllegalStateException(String.format("Module %s (%s) contains multiple default commands, modules may only have a single default.", moduleName, typeName));
                }

                defaultCommand = commandData;
            }

            if (!Collections.disjoint(commandAliases, commandData.getAliases())) {
                String commandName = commandData.getCommand().name();
                String moduleName = module.name();
                String moduleType = handler.getClass().getName();

                throw new IllegalStateException(String.format("Command %s in module %s (%s) contains an alias which has already been registered by a previous command in this module.", commandName, moduleName, moduleType));
            }

            commandAliases.addAll(commandData.getAliases());
            this.commandData.add(commandData);
        }

        Collections.sort(commandData);
    }

    /**
     * @param input The input module by the user.
     * @return If this module contains an entry of that module.
     */
    public boolean performed(String input) {
        return aliases.contains(input.toLowerCase());
    }

    /**
     * Return the command that was performed, or null if
     * the alias has no association with any commands in this
     * module.
     *
     * @param input The input command by the user.
     * @return The command that was performed, else null.
     */
    public CommandData<C, E, M> getCommand(String input) {
        input = input.toLowerCase();

        for (CommandData<C, E, M> commandData : this.commandData) {
            if (commandData.getAliases().contains(input))
                return commandData;
        }

        return null;
    }

    public Commandler<C, E, M> getCommandler() {
        return commandler;
    }

    public IHandler<C, E, M> getHandler() {
        return handler;
    }

    public Module getModule() {
        return module;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public List<String> getAliases() {
        return Collections.unmodifiableList(new ArrayList<>(aliases));
    }

    public List<CommandData<C, E, M>> getCommandData() {
        return commandData;
    }

    /**
     * @return Return all {@link CommandData}s registered to this
     *         module that are {@link CommandData#isPublic() public}.
     */
    public List<CommandData<C, E, M>> getPublicCommands() {
        return commandData.stream().filter(CommandData::isPublic).collect(Collectors.toUnmodifiableList());
    }

    /**
     * @return A list of all {@link Static} commands in the module.
     */
    public List<CommandData<C, E, M>> getStaticCommands() {
        return commandData.stream().filter(CommandData::isStatic).collect(Collectors.toUnmodifiableList());
    }

    public CommandData<C, E, M> getDefaultCommand() {
        return defaultCommand;
    }

    @Override
    public String toString() {
        String format = "%s (%s)";
        StringJoiner commandJoiner = new StringJoiner("\n");

        for (CommandData<C, E, M> commandData : getPublicCommands()) {
            String name = commandData.command.name();
            StringJoiner aliasJoiner = new StringJoiner(", ");

            for (String alias : commandData.getAliases())
                aliasJoiner.add("'" + alias + "'");

            commandJoiner.add(String.format(format, name, aliasJoiner.toString()));
        }

        return commandJoiner.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ModuleData))
            return false;

        return (((ModuleData)o).clazz == clazz);
    }

    @Override
    public int compareTo(ModuleData o) {
        return module.name().compareToIgnoreCase(o.module.name());
    }
}

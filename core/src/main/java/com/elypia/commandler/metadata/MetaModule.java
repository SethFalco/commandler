package com.elypia.commandler.metadata;

import com.elypia.commandler.Commandler;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.impl.IHandler;
import org.slf4j.*;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class MetaModule<C, E, M> implements Comparable<MetaModule> {

    /**
     * This may be thrown when the {@link IHandler} registered to
     * {@link Commandler} does not contain the {@link Module} annotation.
     */
    private static final String NO_MODULE = "Command handler %s isn't annotated with @Module.";

    /**
     * This warning may be logged if the same {@link Module} has multiple of the same
     * alias. This is because it's redundant and the duplicates aren't used.
     */
    private static final String DUPLICATE_ALIASES = "Module {} ({}) contains multiple aliases that are identical.";

    /**
     * This may be thrown if the module registered has a root alias (first argument)
     * which has already been used by a previously registered module or static command.
     */
    private static final String ALIAS_REGISTERED = "Module %s (%s) contains an alias which has already been registered by %s (%s).";

    /**
     * This may be logged if the module registered doesn't have any commands what so ever.
     */
    private static final String NO_COMMANDS = "Module {} ({}) contains no commands.";

    /**
     * This may be thrown if a command registered has an alias which another command
     * in the same module had already registered.
     */
    private static final String DUPLICATE_COMMAND_ALIASES = "Command %s in module %s (%s) contains an alias which has already been registered by a previous command in this module.";

    /**
     * This may be thrown if a module is trying to register multiple {@link Default}
     * commands. Each module can only have up to one default command.
     */
    private static final String MULTIPLE_DEFAULTS = "Module %s (%s) contains multiple default commands, modules may only have a single default.";

    /**
     * We use SLF4J for logging, be sure to include a binding so you can see
     * warnings and messages.
     */
    private static final Logger logger = LoggerFactory.getLogger(MetaModule.class);

    /**
     * Parent {@link Commandler} object this {@link MetaModule} is registered too.
     */
    private Commandler<C, E, M> commandler;

    /**
     * The {@link IHandler} this {@link MetaModule} is describing.
     */
    private IHandler<C, E, M> handler;

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
     * A list of {@link MetaCommand} that were created inside the {@link IHandler}.
     * This does not include {@link Overload}s, these are stored inside
     * {@link MetaCommand#getOverloads()}.
     */
    private List<MetaCommand<C, E, M>> metaCommands;

    /**
     * The {@link Default} command for this module. This value
     * can be null if no default command is specified.
     */
    private MetaCommand<C, E, M> defaultCommand;

    public MetaModule(Commandler<C, E, M> commandler, IHandler<C, E, M> handler) {
        this.commandler = Objects.requireNonNull(commandler);
        this.handler = Objects.requireNonNull(handler);
        module = handler.getClass().getAnnotation(Module.class);

        if (module == null) {
            String className = handler.getClass().getName();
            throw new IllegalStateException(String.format(NO_MODULE, className));
        }

        aliases = new HashSet<>();
        metaCommands = new ArrayList<>();

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

            MetaModule existing = commandler.getRoots().get(alias);

            if (existing != null) {
                String thisModule = module.name();
                String thisClass = handler.getClass().getName();
                String existingModule = existing.module.name();
                String existingClass = existing.handler.getClass().getName();

                throw new IllegalStateException(String.format(ALIAS_REGISTERED, thisModule, thisClass, existingModule, existingClass));
            }

            aliases.add(alias.toLowerCase());
        }

        if (aliases.size() != module.aliases().length)
            logger.warn(DUPLICATE_ALIASES, module.name(), handler.getClass().getName());

        for (String alias : aliases)
            commandler.getRoots().put(alias, this);
    }

    /**
     * Parses the methods in this Module and creates {@link MetaCommand} instances
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
            logger.warn(NO_COMMANDS, module.name(), handler.getClass().getName());

        for (Method method : methods) {
            MetaCommand<C, E, M> metaCommand = new MetaCommand<>(this, method);

            if (metaCommand.isDefault()) {
                if (defaultCommand != null) { // ? If we already registered a default command.
                    String moduleName = module.name();
                    String typeName = handler.getClass().getName();

                    throw new IllegalStateException(String.format(MULTIPLE_DEFAULTS, moduleName, typeName));
                }

                defaultCommand = metaCommand;
            }

            if (!Collections.disjoint(commandAliases, metaCommand.getAliases())) {
                String commandName = metaCommand.getCommand().name();
                String moduleName = module.name();
                String moduleType = handler.getClass().getName();

                throw new IllegalStateException(String.format(DUPLICATE_COMMAND_ALIASES, commandName, moduleName, moduleType));
            }

            commandAliases.addAll(metaCommand.getAliases());
            metaCommands.add(metaCommand);
        }

        Collections.sort(metaCommands);
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
    public MetaCommand<C, E, M> getCommand(String input) {
        input = input.toLowerCase();

        for (MetaCommand<C, E, M> metaCommand : metaCommands) {
            if (metaCommand.getAliases().contains(input))
                return metaCommand;
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

    public List<MetaCommand<C, E, M>> getMetaCommands() {
        return metaCommands;
    }

    /**
     * @return Return all {@link MetaCommand}s registered to this
     *         module that are {@link MetaCommand#isPublic() public}.
     */
    public List<MetaCommand<C, E, M>> getPublicCommands() {
        return metaCommands.stream().filter(MetaCommand::isPublic).collect(Collectors.toUnmodifiableList());
    }

    /**
     * @return A list of all {@link Static} commands in the module.
     */
    public List<MetaCommand<C, E, M>> getStaticCommands() {
        return metaCommands.stream().filter(MetaCommand::isStatic).collect(Collectors.toUnmodifiableList());
    }

    public MetaCommand<C, E, M> getDefaultCommand() {
        return defaultCommand;
    }

    @Override
    public String toString() {
        String format = "%s (%s)";
        StringJoiner commandJoiner = new StringJoiner("\n");

        for (MetaCommand<C, E, M> metaCommand : getPublicCommands()) {
            String name = metaCommand.command.name();
            StringJoiner aliasJoiner = new StringJoiner(", ");

            for (String alias : metaCommand.getAliases())
                aliasJoiner.add("'" + alias + "'");

            commandJoiner.add(String.format(format, name, aliasJoiner.toString()));
        }

        return commandJoiner.toString();
    }

    @Override
    public int compareTo(MetaModule o) {
        return module.name().compareToIgnoreCase(o.module.name());
    }
}

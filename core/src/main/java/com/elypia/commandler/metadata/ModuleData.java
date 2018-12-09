package com.elypia.commandler.metadata;

import com.elypia.commandler.*;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.*;
import org.slf4j.*;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This is a wrapper around a {@link Module} based on the class
 * of the annotated type. This is not an instantiated module that
 * can process commands; it holds information on the commands in a
 * form convinient to access to {@link Commandler} and any other APIs.
 */
public class ModuleData implements Comparable<ModuleData> {

    /**
     * We use SLF4J for logging, be sure to include a binding so you can see
     * warnings and messages.
     */
    private static final Logger logger = LoggerFactory.getLogger(ModuleData.class);

    /**
     * The modules context contains data on all modules that
     * should be registered to this Commandler instance and is reusable.
     *
     * It also contains the context of the modules within it to aid
     * in validation and ensure conflicts don't occur between modules.
     */
    private ModulesContext context;

    /**
     * The class this annotation data belongs too.
     */
    private Class<? extends Handler> moduleClass;

    /**
     * The metadata associated with this annotation.
     */
    private Module annotation;

    /**
     * A list of collected aliases from this annotation. This is used to compare
     * any user input too as we convert all aliases to lower case in advance.
     */
    private Set<String> aliases;

    /**
     * Does this annotation have a description. If the annotation doesn't specify a
     * description, it may be hidden from help commands and pages.
     */
    private boolean isPublic;

    /**
     * A list of {@link CommandData} that were created inside the {@link Handler}.
     * This does not include {@link Overload}s, these are stored inside
     * {@link CommandData#getOverloads()}.
     */
    private List<CommandData> commands;

    private CommandData defaultCommand;

    public ModuleData(ModulesContext context, Class<? extends Handler> moduleClass) {
        this.context = Objects.requireNonNull(context);
        this.moduleClass = Objects.requireNonNull(moduleClass);

        annotation = moduleClass.getAnnotation(Module.class);

        if (annotation == null)
            throw new IllegalStateException(String.format("%s isn't annotated with %s.", moduleClass.getName(), Module.class.getName()));

        isPublic = !annotation.help().equals(Module.HIDEEN);

        aliases = new HashSet<>();
        commands = new ArrayList<>();

        parseAliases();
        parseMethods();
    }

    /**
     * Parse aliases from this annotation, this creates a list of aliases.
     * All aliases on the annotation are converted to lower case and added to this.
     * If find duplicates, we continue filter them however warn the user as this
     * shouldn't be the case. <br>
     * Should an alias be registered that was already registed by another annotation,
     * we throw an exception as this is considered a malformed command.
     * If everything checks out alright, we add all the aliases to the list
     * of {@link ModulesContext#getAliases()}  root aliases} and reserve these so other
     * modules of static commands can't try consume them.
     *
     * @throws IllegalStateException If this annotation has an alias which was already registered by
     * another annotation or static command.
     */
    private void parseAliases() {
        for (String alias : annotation.aliases()) {
            ModuleData existing = context.getModule(alias);

            if (existing != null) {
                String thisModule = annotation.id();
                String thisClass = moduleClass.getName();
                String existingModule = existing.annotation.id();
                String existingClass = existing.getModuleClass().getName();

                throw new IllegalStateException(String.format("Module %s (%s) contains an alias which has already been registered by %s (%s).", thisModule, thisClass, existingModule, existingClass));
            }

            aliases.add(alias.toLowerCase());
        }

        if (aliases.size() != annotation.aliases().length)
            logger.warn("Module {} ({}) contains multiple aliases that are identical.", annotation.id(), moduleClass.getName());
    }

    /**
     * Parses the methods in this Module and creates {@link CommandData} instances
     * out of methods with the {@link Command} annotation. <br>
     * This method ensures only one command in this annotation is a {@link Default default} command
     * if any, and validates all the underlying Commands that are created before adding their aliases
     * to the global list and utlimatly adding the command as valid. <br>
     * <strong>If <em>ANY</em> commands are invalid, the entire annotation will fail.</strong>
     *
     * @throws IllegalStateException When annotation specified more than one command with the {@link Default} annotation.
     */
    private void parseMethods() {
        Set<String> commandAliases = new HashSet<>();
        Method[] methods = moduleClass.getMethods();
        methods = Arrays.stream(methods).filter(m -> m.isAnnotationPresent(Command.class)).toArray(Method[]::new);

        if (methods.length == 0)
            logger.warn("Module {} ({}) contains no commands.", annotation.id(), moduleClass.getName());

        for (Method method : methods) {
            CommandData commandData = new CommandData(this, method);

            if (commandData.isDefault()) {
                if (defaultCommand != null) {
                    String moduleName = annotation.id();
                    String typeName = moduleClass.getName();

                    throw new IllegalStateException(String.format("Module %s (%s) contains multiple default commands, modules may only have a single default.", moduleName, typeName));
                }

                defaultCommand = commandData;
            }

            if (!Collections.disjoint(commandAliases, commandData.getAliases())) {
                String commandName = commandData.getAnnotation().id();
                String moduleName = annotation.id();
                String moduleType = moduleClass.getName();

                throw new IllegalStateException(String.format("Command %s in annotation %s (%s) contains an alias which has already been registered by a previous command in this annotation.", commandName, moduleName, moduleType));
            }

            commandAliases.addAll(commandData.getAliases());
            this.commands.add(commandData);
        }

        Collections.sort(commands);
    }

    /**
     * @param input The input annotation by the user.
     * @return If this annotation contains an entry of that annotation.
     */
    public boolean performed(String input) {
        return aliases.contains(input.toLowerCase());
    }

    public ModulesContext getContext() {
        return context;
    }

    public Class<? extends Handler> getModuleClass() {
        return moduleClass;
    }

    /**
     * Return the command that was performed, or null if
     * the alias has no association with any commands in this
     * annotation.
     *
     * @param input The input command by the user.
     * @return The command that was performed, else null.
     */
    public CommandData getCommand(String input) {
        for (CommandData commandData : this.commands) {
            if (commandData.performed(input))
                return commandData;
        }

        return null;
    }

    public Module getAnnotation() {
        return annotation;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public List<String> getAliases() {
        return List.copyOf(aliases);
    }

    public List<CommandData> getCommands() {
        return List.copyOf(commands);
    }

    /**
     * @return Return all {@link CommandData}s registered to this
     *         annotation that are {@link CommandData#isPublic() public}.
     */
    public List<CommandData> getPublicCommands() {
        return commands.stream().filter(CommandData::isPublic).collect(Collectors.toUnmodifiableList());
    }

    /**
     * @return A list of all {@link Static} commands in the annotation.
     */
    public List<CommandData> getStaticCommands() {
        return commands.stream().filter(CommandData::isStatic).collect(Collectors.toUnmodifiableList());
    }

    public CommandData getDefaultCommand() {
        return defaultCommand;
    }

    @Override
    public String toString() {
        String format = "%s (%s)";
        StringJoiner commandJoiner = new StringJoiner("\n");

        for (CommandData commandData : getPublicCommands()) {
            String name = commandData.command.id();
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

        return (((ModuleData)o).moduleClass == moduleClass);
    }

    @Override
    public int compareTo(ModuleData o) {
        return annotation.id().compareToIgnoreCase(o.annotation.id());
    }
}

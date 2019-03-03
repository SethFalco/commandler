package com.elypia.commandler.metadata.data;

import com.elypia.commandler.*;
import com.elypia.commandler.annotations.Overload;
import com.elypia.commandler.exceptions.MalformedModuleException;
import com.elypia.commandler.metadata.builder.*;
import org.slf4j.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * This is a wrapper around a Module. This is not an instantiated module that
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
     * The class this annotation data belongs too.
     */
    private Class<? extends Handler> clazz;

    /**
     * The name of the module.
     */
    private String name;

    /**
     * The group this module belongs to.
     */
    private String group;

    /**
     * A list of collected aliases from this annotation. This is used to compare
     * any user input too as we convert all aliases to lower case in advance.
     */
    private Set<String> aliases;

    /**
     * Additional help to provide to users.
     */
    private String help;

    /**
     * If the command should be hidden from documentation.
     */
    private boolean isHidden;

    /**
     * A list of {@link CommandData} that were created inside the {@link Handler}.
     * This does not include {@link Overload}s, these are stored inside
     * {@link CommandData#getOverloads()}.
     */
    private List<CommandData> commands;

    private CommandData defaultCommand;

    private Handler instance;

    public ModuleData(Class<? extends Handler> moduleClass, ModuleBuilder builder) {
        try {
            this.clazz = Objects.requireNonNull(moduleClass);
            this.name = Objects.requireNonNull(builder.getName());
            this.group = Objects.requireNonNull(builder.getGroup());
            this.aliases = Objects.requireNonNull(builder.getAliases());
        } catch (NullPointerException ex) {
            throw new MalformedModuleException("Module is missing required metadata.", ex);
        }

        commands = new ArrayList<>();

        for (CommandBuilder command : builder)
            commands.add(command.build(this));

        this.help = builder.getHelp();
    }

    /**
     * @param input The input annotation by the user.
     * @return If this annotation contains an entry of that annotation.
     */
    public boolean performed(String input) {
        return aliases.contains(input.toLowerCase());
    }

    public Class<? extends Handler> getModuleClass() {
        return clazz;
    }

    public String getName() {
        return name;
    }

    public String getHelp() {
        return help;
    }

    public boolean isHidden() {
        return isHidden;
    }

    public Set<String> getAliases() {
        return Set.copyOf(aliases);
    }

    public List<CommandData> getCommands() {
        return List.copyOf(commands);
    }

    /**
     * @return Return all {@link CommandData}s registered to this
     * annotation where {@link CommandData#isHidden()} is false.
     */
    public List<CommandData> getPublicCommands() {
        return commands.stream()
            .filter((command) -> !command.isHidden())
            .collect(Collectors.toUnmodifiableList());
    }

    public List<CommandData> getStaticCommands() {
        return commands.stream()
            .filter(CommandData::isStatic)
            .collect(Collectors.toUnmodifiableList());
    }

    public CommandData getDefaultCommand() {
        return defaultCommand;
    }

    @Override
    public String toString() {
        String format = "%s (%s)";
        StringJoiner commandJoiner = new StringJoiner("\n");

        for (CommandData commandData : getPublicCommands()) {
            String name = commandData.getName();
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
        return name.compareToIgnoreCase(o.name);
    }

    public String getGroup() {
        return group;
    }

    public Handler getInstance() {
        return instance;
    }
}

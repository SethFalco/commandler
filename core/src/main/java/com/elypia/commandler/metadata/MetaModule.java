package com.elypia.commandler.metadata;

import com.elypia.commandler.Commandler;
import com.elypia.commandler.interfaces.Handler;
import org.slf4j.*;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * This is a wrapper around a Module. This is not an instantiated module that
 * can process commands; it holds information on the commands in a
 * form convinient to access to {@link Commandler} and any other APIs.
 */
public class MetaModule implements Comparable<MetaModule>, Iterable<MetaCommand> {

    /** SLF4J logger */
    private static final Logger logger = LoggerFactory.getLogger(MetaModule.class);

    /** The class this annotation data belongs too. */
    private Class<? extends Handler> type;

    /** The groupName this module belongs to. */
    private String groupName;

    /** The name of the module. */
    private String name;

    /** A distinct set of aliases to access this module. */
    private Set<String> aliases;

    /** Additional help to provide to users. */
    private String help;

    /** If the command should be hidden from documentation. */
    private boolean isHidden;

    /** A list of {@link MetaCommand} that were created inside the {@link Handler}. */
    private List<MetaCommand> commands;

    public MetaModule(Class<? extends Handler> moduleClass, String groupName, String name, Set<String> aliases, String help, boolean isHidden, List<MetaCommand> commands) {
        this.type = Objects.requireNonNull(moduleClass);
        this.groupName = Objects.requireNonNull(groupName);
        this.name = Objects.requireNonNull(name);
        this.aliases = Objects.requireNonNull(aliases);
        this.help = help;
        this.isHidden = isHidden;
        this.commands = Objects.requireNonNull(commands);
    }

    /**
     * @param input The input annotation by the user.
     * @return If this annotation contains an entry of that annotation.
     */
    public boolean performed(String input) {
        return aliases.contains(input.toLowerCase());
    }

    public Class<? extends Handler> getHandlerType() {
        return type;
    }

    public String getGroup() {
        return groupName;
    }

    public String getName() {
        return name;
    }

    public Set<String> getAliases() {
        return Set.copyOf(aliases);
    }

    public String getHelp() {
        return help;
    }

    public boolean isHidden() {
        return isHidden;
    }

    public boolean isPublic() {
        return !isHidden;
    }

    public List<MetaCommand> getCommands() {
        return List.copyOf(commands);
    }

    /**
     * @return All {@link MetaCommand}s in this
     * module where {@link MetaCommand#isHidden()} is false.
     */
    public List<MetaCommand> getPublicCommands() {
        return commands.stream()
            .filter(Predicate.not(MetaCommand::isHidden))
            .collect(Collectors.toUnmodifiableList());
    }

    /**
     * @return All {@link MetaCommand}s in this
     * module where {@link MetaCommand#isHidden()} is true.
     */
    public List<MetaCommand> getHiddenCommands() {
        return commands.stream()
            .filter(MetaCommand::isHidden)
            .collect(Collectors.toUnmodifiableList());
    }

    /**
     * @return All commands in this module where
     * {@link MetaCommand#isStatic()} is true.
     */
    public List<MetaCommand> getStaticCommands() {
        return commands.stream()
            .filter(MetaCommand::isStatic)
            .collect(Collectors.toUnmodifiableList());
    }

    /**
     * @return Return the default command, or null
     * if this module doesn't have one.
     */
    public MetaCommand getDefaultCommand() {
        return commands.stream()
            .filter(MetaCommand::isDefault)
            .findAny().orElse(null);
    }

    /**
     * @return The module name and it's aliases.
     */
    @Override
    public String toString() {
        return name + " Module (" + String.join(", ", aliases) + ")";
    }

    /**
     * Sorts {@link MetaModule}s into alphabetical order.
     *
     * @param o Another module.
     * @return If this module is above or below the provided module.
     */
    @Override
    public int compareTo(MetaModule o) {
        return name.compareToIgnoreCase(o.name);
    }

    @Override
    public Iterator<MetaCommand> iterator() {
        return commands.iterator();
    }
}

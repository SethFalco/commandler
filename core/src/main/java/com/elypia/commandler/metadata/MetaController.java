package com.elypia.commandler.metadata;

import com.elypia.commandler.Commandler;
import com.elypia.commandler.api.Controller;
import org.slf4j.*;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * This is a wrapper around a Module. This is not an instantiated module that
 * can process commands; it holds information on the commands in a
 * form convinient to access to {@link Commandler} and any other APIs.
 */
public class MetaController extends MetaComponent implements Comparable<MetaController>, Iterable<MetaCommand> {

    /** SLF4J logger */
    private static final Logger logger = LoggerFactory.getLogger(MetaController.class);

    /** The class this annotation data belongs too. */
    private Class<? extends Controller> type;

    /** The groupName this module belongs to. */
    private String groupName;

    /** If the command should be hidden from documentation. */
    private boolean isHidden;

    /** A list of {@link MetaCommand} that were created inside the {@link Controller}. */
    private List<MetaCommand> metaCommands;

    public MetaController(Class<? extends Controller> moduleClass, String groupName, String name, String description, boolean isHidden, Properties properties, List<MetaCommand> metaCommands) {
        this.type = Objects.requireNonNull(moduleClass);
        this.groupName = Objects.requireNonNull(groupName);
        this.name = Objects.requireNonNull(name);
        this.description = description;
        this.isHidden = isHidden;
        this.properties = properties;
        this.metaCommands = Objects.requireNonNull(metaCommands);
    }

    public Class<? extends Controller> getHandlerType() {
        return type;
    }

    public String getGroup() {
        return groupName;
    }

    public boolean isHidden() {
        return isHidden;
    }

    public boolean isPublic() {
        return !isHidden;
    }

    public List<MetaCommand> getMetaCommands() {
        return List.copyOf(metaCommands);
    }

    /**
     * @return All {@link MetaCommand}s in this
     * module where {@link MetaCommand#isHidden()} is false.
     */
    public List<MetaCommand> getPublicCommands() {
        return metaCommands.stream()
            .filter(Predicate.not(MetaCommand::isHidden))
            .collect(Collectors.toUnmodifiableList());
    }

    /**
     * @return All {@link MetaCommand}s in this
     * module where {@link MetaCommand#isHidden()} is true.
     */
    public List<MetaCommand> getHiddenCommands() {
        return metaCommands.stream()
            .filter(MetaCommand::isHidden)
            .collect(Collectors.toUnmodifiableList());
    }

    /**
     * @return All commands in this module where
     * {@link MetaCommand#isStatic()} is true.
     */
    public List<MetaCommand> getStaticControls() {
        return metaCommands.stream()
            .filter(MetaCommand::isStatic)
            .collect(Collectors.toUnmodifiableList());
    }

    /**
     * @return Return the default command, or null
     * if this module doesn't have one.
     */
    public MetaCommand getDefaultControl() {
        return metaCommands.stream()
            .filter(MetaCommand::isDefault)
            .findAny().orElse(null);
    }

    /**
     * Sorts {@link MetaController}s into alphabetical order.
     *
     * @param o Another module.
     * @return If this module is above or below the provided module.
     */
    @Override
    public int compareTo(MetaController o) {
        return name.compareToIgnoreCase(o.name);
    }

    @Override
    public Iterator<MetaCommand> iterator() {
        return metaCommands.iterator();
    }
}

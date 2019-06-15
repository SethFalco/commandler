package com.elypia.commandler.metadata.data;

import com.elypia.commandler.metadata.builder.ParamBuilder;
import org.slf4j.*;

import java.lang.reflect.Method;
import java.util.*;

public class MetaCommand implements Comparable<MetaCommand>, Iterable<MetaParam> {

    /** We use SLF4J for logging, be sure to include an implementation / binding. */
    private static final Logger logger = LoggerFactory.getLogger(MetaCommand.class);

    /** The actual method that is called when this command is performed. */
    private Method method;

    /** The display-friendly name of this command. */
    private String name;

    /** A distinct list of aliases for this command. */
    private Set<String> aliases;

    /** A short helper description or message for what this command does. */
    private String help;

    /** If this command is hidden from public help messages. */
    private boolean isHidden;

    /** Is this a static command, if so it can be performed without specifying the module. */
    private boolean isStatic;

    /** If this is the default command of the module. */
    private boolean isDefault;

    /** The parameters this command requires. */
    private List<MetaParam> params;

    public MetaCommand(Method method, String name, Set<String> aliases, String help, boolean isHidden, boolean isStatic, boolean isDefault) {
        this.method = Objects.requireNonNull(method);
        this.name = Objects.requireNonNull(name);
        this.aliases = Objects.requireNonNull(aliases);
        this.help = help;
        this.isHidden = isHidden;
        this.isStatic = isStatic;
        this.isDefault = isDefault;

        params = new ArrayList<>();

        for (ParamBuilder param : builder)
            params.add(param.build(this));
    }

    /**
     * @param input The input module by the user.
     * @return If this command contains an entry of that command.
     */
    public boolean performed(String input) {
        return aliases.contains(input.toLowerCase());
    }

    public Method getMethod() {
        return method;
    }

    public List<MetaParam> getParams() {
        return Collections.unmodifiableList(params);
    }

    public Set<String> getAliases() {
        return aliases;
    }

    public boolean isStatic() {
        return isStatic;
    }

    public boolean isDefault() {
        return isDefault;
    }

    @Override
    public String toString() {
        return name + " " + toParamsString();
    }

    public String toParamsString() {
        if (params == null)
            return "Uninitialized";

        if (params.isEmpty())
            return "(0) None";

        StringJoiner itemJoiner = new StringJoiner(", ");

        for (MetaParam param : params) {
            String name = param.getName();

            if (param.isList())
                itemJoiner.add("['" + name + "']");
            else
                itemJoiner.add("'" + name + "'");
        }

        return "(" + params.size() + ") " + itemJoiner.toString();
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

    /**
     * Sorts {@link MetaCommand}s into alphabetical order.
     *
     * @param o Another command.
     * @return If this command is above or below the provided command.
     */
    @Override
    public int compareTo(MetaCommand o) {
        return name.compareToIgnoreCase(o.name);
    }

    @Override
    public Iterator<MetaParam> iterator() {
        return params.iterator();
    }
}

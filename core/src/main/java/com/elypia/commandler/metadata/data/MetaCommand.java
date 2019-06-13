package com.elypia.commandler.metadata.data;

import com.elypia.commandler.annotations.*;
import com.elypia.commandler.metadata.builder.*;
import org.slf4j.*;

import java.lang.reflect.Method;
import java.util.*;

public class MetaCommand implements Comparable<MetaCommand> {

    /**
     * We use SLF4J for logging, be sure to include an implementation / binding
     * so you can configure warnings and other messages.
     */
    private static final Logger logger = LoggerFactory.getLogger(MetaCommand.class);

    /**
     * The actual method that is called when this command is performed.
     */
    private Method method;

    private String name;

    /**
     * A list of unique aliases for this command. If the command
     * had duplicate aliases, they are filtered out.
     */
    private Set<String> aliases;

    private String help;

    private boolean isHidden;

    /**
     * Is this a {@link Static} command. <br>
     * Static commands can be performed without specifying the
     * module it belongs too, despite this it still belongs to the module.
     */
    private boolean isStatic;

    /**
     * Is this a {@link Default} command. <br>
     * Default commands are what we assume if the user didn't specify a command
     * or the next input after the module name doesn't fit the name requirement
     * or alias of any commands in the alias.
     */
    private boolean isDefault;

    /**
     * The parameters this command requires.
     */
    private List<ParamData> params;

    private List<ParamData> defaultParams;
    private List<OverloadData> overloads;

    public MetaCommand(CommandBuilder builder) {
        Objects.requireNonNull(builder);
        this.method = Objects.requireNonNull(builder.getMethod());
        this.name = Objects.requireNonNull(builder.getName());
        this.help = Objects.requireNonNull(builder.getHelp());
        this.aliases = Objects.requireNonNull(builder.getAliases());
        this.isHidden = builder.isHidden();
        this.isStatic = builder.isStatic();
        this.isDefault = builder.isDefault();

        params = new ArrayList<>();
        defaultParams = new ArrayList<>();
        overloads = new ArrayList<>();

        for (ParamBuilder param : builder)
            params.add(param.build(this));

        for (String name : builder.getDefaultParams())
            defaultParams.add(params.stream().filter(o -> o.getName().equals(name)).findAny().get());

        for (OverloadBuilder overload : builder.getOverloads())
            overloads.add(overload.build(this));
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

    public List<ParamData> getParams() {
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

        for (ParamData param : params) {
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

    public List<OverloadData> getOverloads() {
        return overloads;
    }

    public List<ParamData> getDefaultParams() {
        return defaultParams;
    }

    @Override
    public int compareTo(MetaCommand o) {
        return name.compareToIgnoreCase(o.name);
    }
}

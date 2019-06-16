package com.elypia.commandler.meta.builder;

import com.elypia.commandler.meta.data.*;
import org.slf4j.*;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.*;

public class CommandBuilder implements Iterable<ParamBuilder> {

    /** SLF4J Logger */
    private static final Logger logger = LoggerFactory.getLogger(CommandBuilder.class);

    /** The method to execute when this command is called. */
    private Method method;

    /** The user friendly name of command. */
    private String name;

    /** Aliases to access this command. */
    private Set<String> aliases;

    /** A help descriptor to help users use this command. */
    private String help;

    /** Is this command displayed on help messages and docs. */
    private boolean isHidden;

    /** Is this the default command if none is specified. */
    private boolean isDefault;

    /** Is this a static command. */
    private boolean isStatic;

    /** Are the parameters in the method head, or in a ParamObject. */
    private boolean isInline;

    private Collection<ParamBuilder> params;

    public CommandBuilder(Method method) {
        this.method = method;
        params = new ArrayList<>();
        params = new ArrayList<>();
    }

    public MetaCommand build(MetaModule data) {
        if (isStatic) {
            Set<String> existing = data.getAliases();

            for (String alias : aliases) {
                if (!existing.contains(alias))
                    continue;

                String format = "Static Command `%s` has alias `%s` which is already registered by another module or static command.";
                throw new IllegalStateException(String.format(format, name, alias));
            }
        }

        // TODO: Somehow give this the parameters too.
        return new MetaCommand(method, name, aliases, help, isHidden, isStatic, isDefault, null);
    }

    public Method getMethod() {
        return method;
    }

    public String getName() {
        return name;
    }

    public CommandBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public Set<String> getAliases() {
        return aliases;
    }

    public CommandBuilder setAliases(String... aliases) {
        this.aliases = Stream.of(aliases)
            .map(String::toLowerCase).collect(Collectors.toSet());

        if (this.aliases.size() != aliases.length)
            logger.warn("Command `%s` contains multiple of the same alias.");

        return this;
    }

    public String getHelp() {
        return help;
    }

    public CommandBuilder setHelp(String help) {
        this.help = help;
        return this;
    }

    public boolean isHidden() {
        return isHidden;
    }

    public CommandBuilder setHidden(boolean hidden) {
        isHidden = hidden;
        return this;
    }

    public boolean isStatic() {
        return isStatic;
    }

    public CommandBuilder setStatic(boolean aStatic) {
        isStatic = aStatic;
        return this;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public CommandBuilder setDefault(boolean aDefault) {
        isDefault = aDefault;
        return this;
    }

    public Collection<ParamBuilder> getParams() {
        return params;
    }

    public CommandBuilder setParams(ParamBuilder... params) {
        return setParams(List.of(params));
    }

    public CommandBuilder setParams(Collection<ParamBuilder> params) {
        this.params = params;
        return this;
    }

    @Override
    public Iterator<ParamBuilder> iterator() {
        return params.iterator();
    }
}

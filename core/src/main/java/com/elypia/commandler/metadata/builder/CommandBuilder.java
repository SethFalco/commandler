package com.elypia.commandler.metadata.builder;

import com.elypia.commandler.metadata.data.*;
import org.slf4j.*;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.*;

public class CommandBuilder implements Iterable<ParamBuilder> {

    private static final Logger logger = LoggerFactory.getLogger(CommandBuilder.class);

    private Method method;
    private String name;
    private Set<String> aliases;
    private String help;
    private boolean isHidden;
    private boolean isStatic;
    private boolean isDefault;
    private List<String> defaultParams;

    private Collection<ParamBuilder> params;
    private Collection<OverloadBuilder> overloads;

    public CommandBuilder(Method method) {
        this.method = method;
        params = new ArrayList<>();
        defaultParams = new ArrayList<>();
        overloads = new ArrayList<>();
    }

    public CommandBuilder addParam(ParamBuilder param) {
        params.add(param);
        defaultParams.add(param.getName());
        return this;
    }

    public CommandBuilder addOverload(OverloadBuilder overload) {
        params.addAll(overload.getParams());
        overloads.add(overload);
        return this;
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

        return new MetaCommand(this);
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

    public Collection<String> getDefaultParams() {
        return defaultParams;
    }

    public Collection<OverloadBuilder> getOverloads() {
        return overloads;
    }

    @Override
    public Iterator<ParamBuilder> iterator() {
        return params.iterator();
    }
}

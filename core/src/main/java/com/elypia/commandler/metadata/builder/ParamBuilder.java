package com.elypia.commandler.metadata.builder;

import com.elypia.commandler.metadata.data.*;

public class ParamBuilder {

    private Class<?> type;
    private String name;
    private String help;
    private String defaultValue;

    public ParamBuilder(Class<?> type) {
        this.type = type;
    }

    public MetaParam build(MetaCommand data) {
        return new MetaParam(type, name, help, defaultValue);
    }

    public Class<?> getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public ParamBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public String getHelp() {
        return help;
    }

    public ParamBuilder setHelp(String help) {
        this.help = help;
        return this;
    }
}

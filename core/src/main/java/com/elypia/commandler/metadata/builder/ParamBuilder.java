package com.elypia.commandler.metadata.builder;

import com.elypia.commandler.metadata.data.*;

import java.lang.reflect.Parameter;

public class ParamBuilder {

    private Parameter parameter;
    private String name;
    private String help;

    public ParamBuilder(Parameter parameter) {
        this.parameter = parameter;
    }

    public ParamData build(CommandData data) {
        return new ParamData(parameter, this);
    }

    public Parameter getParameter() {
        return this.parameter;
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

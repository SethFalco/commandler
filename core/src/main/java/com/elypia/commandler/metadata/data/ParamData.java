package com.elypia.commandler.metadata.data;

import com.elypia.commandler.interfaces.CommandlerEvent;
import com.elypia.commandler.metadata.builder.ParamBuilder;

import java.lang.reflect.Parameter;
import java.util.Objects;

public class ParamData {

    /**
     * The method paramater this is for. This could be any object including
     * {@link CommandlerEvent}.
     */
    private Parameter parameter;

    private String name;
    private String help;

    private boolean isList;

    public ParamData(Parameter parameter, ParamBuilder builder) {
        this.parameter = Objects.requireNonNull(parameter);
        this.name = Objects.requireNonNull(builder.getName());
        this.help = builder.getHelp();

        isList = parameter.getType().isArray();
    }

    public Parameter getParameter() {
        return parameter;
    }

    public String getName() {
        return name;
    }

    public String getHelp() {
        return help;
    }

    public boolean isList() {
        return isList;
    }
}

package com.elypia.commandler.metadata;

import com.elypia.commandler.Commandler;
import com.elypia.commandler.annotations.Param;
import com.elypia.commandler.interfaces.ICommandEvent;

import java.lang.reflect.Parameter;
import java.util.Objects;

public class ParamData {

    private CommandData commandData;

    /**
     * The method paramater this is for. This could be any object including
     * {@link ICommandEvent}.
     */
    private Parameter parameter;

    /**
     * The param annotation associated with this {@link #parameter} which gives us more
     * information to generate things like the help command. <br>
     * Note: This <strong>WILL</strong> be null if the {@link #parameter} is {@link ICommandEvent}.
     */
    private Param annotation;

    /**
     * If this parameter is something that {@link Commandler} will populate
     * or if it something that contirbutes towards command parameters and required
     * user input.
     */
    private boolean isInput;

    private boolean isList;

    ParamData(CommandData commandData, Param annotation, Parameter parameter) {
        this.commandData = Objects.requireNonNull(commandData);
        this.annotation = annotation;
        this.parameter = Objects.requireNonNull(parameter);

        isInput = !ICommandEvent.class.isAssignableFrom(parameter.getType());
        isList = parameter.getType().isArray();
    }

    public CommandData getCommandData() {
        return commandData;
    }

    public Parameter getParameter() {
        return parameter;
    }

    public Param getAnnotation() {
        return annotation;
    }

    public boolean isInput() {
        return isInput;
    }

    public boolean isList() {
        return isList;
    }
}

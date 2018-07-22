package com.elypia.commandler.metadata;

import com.elypia.commandler.*;
import com.elypia.commandler.annotations.Param;
import com.elypia.commandler.annotations.validation.Validation;
import com.elypia.commandler.impl.IParamValidator;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.util.*;

public class MetaParam {

    /**
     * The parent Commandler instance that spawned this MetaParam.
     */

    private Commandler commandler;

    private MetaCommand metaCommand;

    /**
     * The method paramater this is for. This could be any object including
     * {@link CommandEvent}.
     */

    private Parameter parameter;

    /**
     * The param annotation associated with this {@link #parameter} which gives us more
     * information to generate things like the help command. <br>
     * Note: This <strong>WILL</strong> be null if the {@link #parameter} is {@link CommandEvent}.
     */

    private Param annotation;

    /**
     * If this parameter is something that {@link Commandler} will populate
     * or if it something that contirbutes towards command parameters and required
     * user input.
     */

    private boolean isInput;

    private boolean isList;

    Map<MetaValidator, IParamValidator> validators;

    MetaParam(MetaCommand metaCommand, Param annotation, Parameter parameter) {
        this.metaCommand = Objects.requireNonNull(metaCommand);
        this.annotation = annotation;
        this.parameter = Objects.requireNonNull(parameter);

        commandler = metaCommand.getCommandler();
        isInput = !CommandEvent.class.isAssignableFrom(parameter.getType());
        isList = parameter.getType().isArray();

        parseAnnotations();
    }

    /**
     * Parse all annotations on this parameter and collects any validation annotations
     * associated with it.
     */

    private void parseAnnotations() {
        validators = new HashMap<>();

        for (Annotation annotation : parameter.getDeclaredAnnotations()) {
            Class<? extends Annotation> type = annotation.annotationType();

            if (type.isAnnotationPresent(Validation.class)) {
                IParamValidator validator = commandler.getValidator().getParamValidators().get(type);

                if (validator == null)
                    throw new IllegalStateException(String.format("Command %s in module %s (%s) has a parameter with the %s annotation, but a validator of this type is not registered.", metaCommand.getCommand().name(), metaCommand.getMetaModule().getModule().name(), "temp", annotation.annotationType().getName()));

                validators.put(new MetaValidator(annotation), validator);
            }
        }
    }

    public Commandler getCommandler() {
        return commandler;
    }

    public MetaCommand getMetaCommand() {
        return metaCommand;
    }

    public Parameter getParameter() {
        return parameter;
    }

    public Map<MetaValidator, IParamValidator> getValidators() {
        return validators;
    }

    public Param getParamAnnotation() {
        return annotation;
    }

    public boolean isInput() {
        return isInput;
    }

    public boolean isList() {
        return isList;
    }
}

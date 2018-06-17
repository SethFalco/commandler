package com.elypia.commandler.metadata;

import com.elypia.commandler.Commandler;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.events.MessageEvent;
import com.elypia.commandler.validation.IParamValidator;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.util.*;

public class MetaParam {

    /**
     * The parent Commandler instance that spawned this MetaParam.
     */

    private Commandler commandler;

    /**
     * The parent Module / CommandHandler that spawned this MetaParam.
     */

    private MetaModule metaModule;

    /**
     * The MetaCommand this MetaParameter belongs to.
     */

    private MetaCommand metaCommand;

    /**
     * The method paramater this is for. This could be any object including
     * {@link MessageEvent}.
     */

    private Parameter parameter;

    /**
     * The param annotation associated with this {@link #parameter} which gives us more
     * information to generate things like the help command. <br>
     * Note: This <strong>WILL</strong> be null if the {@link #parameter} is {@link MessageEvent}.
     */

    private Param paramAnnotation;

    /**
     * If this parameter is something that {@link Commandler} will populate
     * or if it something that contirbutes towards command parameters and required
     * user input.
     */

    private boolean isInput;

    /**
     * A map containing all validation annotations associated with this parameter.
     */

    private Map<Class<? extends Annotation>, MetaValidator> validators;

    /**
     * Wrap around this parameter for convinience functions.
     *
     * @param metaCommand The wrapper object around the method this parameter belongs to.
     * @param parameter The standard paramater as in the method.
     * @param annotation The paramater annotation decorated on the command.
     * @return A wrapper object around this paramater to obtain information around it.
     */

    protected static MetaParam of(MetaCommand metaCommand, Parameter parameter, Param annotation) {
        return new MetaParam(metaCommand, parameter, annotation);
    }

    private MetaParam(MetaCommand metaCommand, Parameter parameter, Param annotation) {
        this.parameter = parameter;
        this.paramAnnotation = annotation;
        commandler = metaCommand.getCommandler();
        metaModule = metaCommand.getMetaModule();
        this.metaCommand = metaCommand;

        parseAnnotations();

        isInput = parameter.getType() != MessageEvent.class;
    }

    /**
     * Parse all annotations on this parameter and collects any validation annotations
     * associated with it.
     */

    private void parseAnnotations() {
        validators = new HashMap<>();

        for (Annotation annotation : parameter.getDeclaredAnnotations()) {
            Class<? extends Annotation> type = annotation.annotationType();

            if (type.isAnnotationPresent(Validation.class))
                validators.put(type, MetaValidator.of(annotation));
        }
    }

    /**
     * This will only return any validators that may have been registered at the time
     * this method is called, even if the @Validation Annotation is found, a pairing
     * {@link IParamValidator} must be registered via {@link Commandler#registerValidator(Class, IParamValidator)}.
     *
     * @return A list of validators associated with this {@link Param}.
     */

    public Map<MetaValidator, IParamValidator> getValidators() {
        Map<MetaValidator, IParamValidator> validatorMap = new HashMap<>();

        commandler.getDispatcher().getValidator().getParamValidators().forEach((type, validator) -> {
            validators.forEach((typeII, metaCommandValidator) -> {
                if (type == typeII)
                    validatorMap.put(metaCommandValidator, validator);
            });
        });

        return validatorMap;
    }

    public Commandler getCommandler() {
        return commandler;
    }

    public MetaModule getMetaModule() {
        return metaModule;
    }

    public MetaCommand getMetaCommand() {
        return metaCommand;
    }

    public Parameter getParameter() {
        return parameter;
    }

    public Param getParamAnnotation() {
        return paramAnnotation;
    }

    public boolean isInput() {
        return isInput;
    }
}

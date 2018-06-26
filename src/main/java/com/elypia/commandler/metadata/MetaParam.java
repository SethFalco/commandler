package com.elypia.commandler.metadata;

import com.elypia.commandler.Commandler;
import com.elypia.commandler.annotations.Param;
import com.elypia.commandler.annotations.validation.Validation;
import com.elypia.commandler.events.CommandEvent;
import com.elypia.commandler.exceptions.MalformedCommandException;
import com.elypia.commandler.modules.CommandHandler;
import com.elypia.commandler.validation.IParamValidator;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.util.*;

public class MetaParam {

    /**
     * The parent Commandler instance that spawned this MetaParam.
     */

    private Commandler commandler;

    private AbstractMetaCommand abstractMetaCommand;

    private Class<? extends CommandHandler> clazz;

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

    private Param paramAnnotation;

    /**
     * If this parameter is something that {@link Commandler} will populate
     * or if it something that contirbutes towards command parameters and required
     * user input.
     */

    private boolean isInput;

    Map<MetaValidator, IParamValidator> validators;

    /**
     * Wrap around this parameter for convinience functions.
     *
     * @param abstractMetaCommand The wrapper object around the module that contains this parameter.
     * @param parameter The standard paramater as in the method.
     * @param annotation The paramater annotation decorated on the command.
     * @return A wrapper object around this paramater to obtain information around it.
     */

    protected static MetaParam of(AbstractMetaCommand abstractMetaCommand, Param annotation, Parameter parameter) {
        return new MetaParam(abstractMetaCommand, annotation, parameter);
    }

    private MetaParam(AbstractMetaCommand abstractMetaCommand, Param annotation, Parameter parameter) {
        this.abstractMetaCommand = Objects.requireNonNull(abstractMetaCommand);
        this.parameter = Objects.requireNonNull(parameter);
        this.paramAnnotation = annotation;

        clazz = abstractMetaCommand.getHandlerType();
        commandler = abstractMetaCommand.getCommandler();
        isInput = parameter.getType() != CommandEvent.class;

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
                IParamValidator validator = commandler.getDispatcher().getValidator().getParamValidators().get(type);

                if (validator == null)
                    throw new MalformedCommandException(String.format("Command %s in module %s (%s) has a parameter with the %s annotation, but a validator of this type is not registered.", abstractMetaCommand.getCommand().name(), abstractMetaCommand.getMetaModule().getModule().name(), clazz.getName(), annotation.annotationType().getName()));

                validators.put(MetaValidator.of(annotation), validator);
            }
        }
    }

    public Commandler getCommandler() {
        return commandler;
    }

    public AbstractMetaCommand getAbstractMetaCommand() {
        return abstractMetaCommand;
    }

    public Parameter getParameter() {
        return parameter;
    }

    public Map<MetaValidator, IParamValidator> getValidators() {
        return validators;
    }

    public Param getParamAnnotation() {
        return paramAnnotation;
    }

    public boolean isInput() {
        return isInput;
    }
}

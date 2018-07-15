package com.elypia.commandler.metadata;

import com.elypia.commandler.Commandler;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.annotations.validation.Validation;
import com.elypia.commandler.exceptions.MalformedCommandException;
import com.elypia.commandler.modules.CommandHandler;
import com.elypia.commandler.impl.ICommandValidator;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public abstract class AbstractMetaCommand {

    /**
     * The parent {@link Commandler} instance that spawned this object.
     */

    protected Commandler commandler;

    /**
     * The {@link MetaModule meta data} of the parent {@link CommandHandler}
     * that spawned this {@link MetaCommand}.
     */

    protected MetaModule metaModule;

    protected CommandHandler handler;

    /**
     * The type of command handler we are handling.
     */

    protected Class<? extends CommandHandler> clazz;

    /**
     * The {@link Command} annotation associated with this command. <br>
     * This is what defines a method as a command in a Command Handler.
     */

    protected Command command;

    /**
     * The method this is pulling metadata from.
     */

    protected Method method;

    /**
     * A map of any annotations added to this command, this may include
     * module level annotations.
     */

    protected Map<Class<? extends Annotation>, Annotation> validationAnnotations;

    /**
     * A list of all validators associated with this command.
     * A validator is dictated by whether the annotation itself
     * has the {@link Validation} annotation.
     */

    protected Map<MetaValidator, ICommandValidator> validators;

    /**
     * A list of any {@link MetaParam params} this command required to execute.
     */

    protected List<MetaParam> metaParams;

    /**
     * The amount of input required to perform this command.
     */

    protected int inputRequired;

    public AbstractMetaCommand(MetaModule metaModule, Method method) {
        this.metaModule = Objects.requireNonNull(metaModule);
        this.method = Objects.requireNonNull(method);

        commandler = metaModule.getCommandler();
        handler = metaModule.getHandler();
        clazz = metaModule.getHandlerType();

        parseParams();
        parseAnnotations();
        parseValidators();
    }

    protected abstract void parseParams();

    protected abstract void parseAnnotations();

    protected void parseValidators() {
        validators = new HashMap<>();

        validationAnnotations.forEach((type, annotation) -> {
            ICommandValidator validator = commandler.getDispatcher().getValidator().getCommandValidators().get(type);

            if (validator != null)
                validators.put(MetaValidator.of(annotation), validator);
            else
                throw new MalformedCommandException(String.format("Command in module %s (%s) has the %s annotation, but a validator of this type is not registered.", metaModule.getModule().name(), clazz.getName(), type.getName()));
        });
    }

    public List<MetaParam> getInputParams() {
        return metaParams.stream().filter(MetaParam::isInput).collect(Collectors.toList());
    }

    public Commandler getCommandler() {
        return commandler;
    }

    public CommandHandler getHandler() {
        return handler;
    }

    public MetaModule getMetaModule() {
        return metaModule;
    }

    public Class<? extends CommandHandler> getHandlerType() {
        return clazz;
    }

    public Command getCommand() {
        return command;
    }

    public Method getMethod() {
        return method;
    }

    public Map<MetaValidator, ICommandValidator> getValidators() {
        return validators;
    }

    public List<MetaParam> getMetaParams() {
        return metaParams;
    }

    public int getInputRequired() {
        return inputRequired;
    }
}

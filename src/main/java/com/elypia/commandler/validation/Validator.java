package com.elypia.commandler.validation;

import com.elypia.commandler.Commandler;
import com.elypia.commandler.annotations.Validation;
import com.elypia.commandler.annotations.validation.command.*;
import com.elypia.commandler.annotations.validation.param.*;
import com.elypia.commandler.events.MessageEvent;
import com.elypia.commandler.exceptions.MalformedValidatorException;
import com.elypia.commandler.metadata.*;
import com.elypia.commandler.validation.command.*;
import com.elypia.commandler.validation.param.*;

import java.lang.annotation.Annotation;
import java.util.*;

/**
 * Vaidates annotations associated with commands paremeters to ensure
 * they are within the bounds that is specified relative to the user
 * performing the commands.
 */

public class Validator {

    private Commandler commandler;

    private Map<Class<? extends Annotation>, ICommandValidator> commandValidators;
    private Map<Class<? extends Annotation>, IParamValidator> paramValidators;


    public Validator(Commandler commandler) {
        this(commandler, true);
    }

    public Validator(Commandler commandler, boolean auto) {
        this.commandler = commandler;
        commandValidators = new HashMap<>();
        paramValidators = new HashMap<>();

        if (auto) {
            registerValidator(Elevated.class, new ElevatedValidator());
            registerValidator(NSFW.class, new NSFWValidator());
            registerValidator(Permissions.class, new PermissionValidator());
            registerValidator(Scope.class, new ScopeValidator());

            registerValidator(Length.class, new LengthValidator());
            registerValidator(Limit.class, new LimitValidator());
            registerValidator(Option.class, new OptionValidator());
        }
    }

    public void registerValidator(Class<? extends Annotation> clazz, IParamValidator validator) {
        registerValidator(clazz);

        if (paramValidators.put(clazz, validator) != null)
            System.err.printf("The previous %s validator has been overwritten for the new implementation provided.\n", clazz.getName());
    }

    public void registerValidator(Class<? extends Annotation> clazz, ICommandValidator validator) {
        registerValidator(clazz);

        if (commandValidators.put(clazz, validator) != null)
            System.err.printf("The previous %s validator has been overwritten for the new implementation provided.\n", clazz.getName());
    }

    private void registerValidator(Class<? extends Annotation> clazz) {
        if (!clazz.isAnnotationPresent(Validation.class)) {
            String message = String.format("Annotation for %s doesn't have the Validator annotation.", clazz.getName());
            throw new MalformedValidatorException(message);
        }
    }

    public boolean validateCommand(MessageEvent event, MetaCommand metaCommand) {
        for (Map.Entry<MetaValidator, ICommandValidator> entry : metaCommand.getValidators().entrySet()) {
            if (!entry.getValue().validate(event, entry.getKey().getAnnotation()))
                return false;
        }

        return true;
    }

    public boolean validateParams(MessageEvent event, MetaCommand metaCommand, Object[] args) {
        List<MetaParam> metaParams = metaCommand.getMetaParams();

        for (int i = 0; i < metaParams.size(); i++) {
            MetaParam metaParam = metaParams.get(i);
            Map<MetaValidator, IParamValidator> validators = metaParam.getValidators();

            for (Map.Entry<MetaValidator, IParamValidator> entry : validators.entrySet()) {
                Object arg = args[i];
                if (!entry.getValue().validate(event, arg.getClass().cast(arg), entry.getKey().getAnnotation(), metaParam))
                    return false;
            }
        }

        return true;
    }

    public Map<Class<? extends Annotation>, IParamValidator> getParamValidators() {
        return paramValidators;
    }

    public Map<Class<? extends Annotation>, ICommandValidator> getCommandValidators() {
        return commandValidators;
    }
}

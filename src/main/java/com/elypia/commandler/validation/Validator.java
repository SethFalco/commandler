package com.elypia.commandler.validation;

import com.elypia.commandler.Commandler;
import com.elypia.commandler.annotations.validation.command.*;
import com.elypia.commandler.annotations.validation.param.*;
import com.elypia.commandler.events.MessageEvent;
import com.elypia.commandler.metadata.*;
import com.elypia.commandler.validation.command.*;
import com.elypia.commandler.validation.param.*;

import java.lang.annotation.Annotation;
import java.util.*;

/**
 * Vaidates annotations associated with command paremeters to ensure
 * they are within the bounds that is specified relative to the user
 * performing the command.
 */

public class Validator {

    private Commandler commandler;

    private Map<Class<? extends Annotation>, IParamValidator> paramValidators;
    private Map<Class<? extends Annotation>, ICommandValidator> commandValidators;

    public Validator(Commandler commandler) {
        this(commandler, true);
    }

    public Validator(Commandler commandler, boolean auto) {
        this.commandler = commandler;
        paramValidators = new HashMap<>();
        commandValidators = new HashMap<>();

        if (auto) {
            registerValidator(Length.class, new LengthValidator());
            registerValidator(Limit.class, new LimitValidator());
            registerValidator(Option.class, new OptionValidator());

            registerValidator(NSFW.class, new NSFWValidator());
            registerValidator(Scope.class, new ScopeValidator());
            registerValidator(Permissions.class, new PermissionValidator());
            registerValidator(Elevated.class, new ElevatedValidator());
        }
    }

    public void registerValidator(Class<? extends Annotation> clazz, IParamValidator validator) {
        if (paramValidators.keySet().contains(clazz))
            throw new IllegalArgumentException("Parameter validator for this type of annotation has already been registered.");

        paramValidators.put(clazz, validator);
    }

    public void registerValidator(Class<? extends Annotation> clazz, ICommandValidator validator) {
        if (commandValidators.keySet().contains(clazz))
            throw new IllegalArgumentException("Command validator for this type of annotation has already been registered.");

        commandValidators.put(clazz, validator);
    }

    public void validate(MessageEvent event, MetaCommand metaCommand, Object[] args) throws IllegalArgumentException, IllegalAccessException {
        for (Map.Entry<Class<? extends Annotation>, Annotation> entry : metaCommand.getAnnotations().entrySet()) {
            ICommandValidator validator = commandValidators.get(entry.getKey());

            if (validator != null)
                validator.validate(event, entry.getValue());
        }

        List<MetaParam> params = metaCommand.getMetaParams();

        for (int i = 0; i < params.size(); i++) {
            MetaParam parameter = params.get(i);
            Annotation[] annotations = parameter.getAnnotations();

            for (int ii = 0; ii < annotations.length; ii++) {
                Annotation a = annotations[ii];
                Class<?> clazz = a.annotationType();
                IParamValidator validator = paramValidators.get(clazz);

                if (validator != null)
                    validator.validate(args[i].getClass().cast(args[i]), a, parameter);
            }
        }
    }

    public Map<Class<? extends Annotation>, IParamValidator> getParamValidators() {
        return paramValidators;
    }

    public Map<Class<? extends Annotation>, ICommandValidator> getCommandValidators() {
        return commandValidators;
    }
}

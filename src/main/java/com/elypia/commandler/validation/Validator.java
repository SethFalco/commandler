package com.elypia.commandler.validation;

import com.elypia.commandler.annotations.validation.Length;
import com.elypia.commandler.annotations.validation.Limit;
import com.elypia.commandler.events.MessageEvent;
import com.elypia.commandler.metadata.MetaCommand;
import com.elypia.commandler.metadata.MetaParam;
import com.elypia.commandler.validation.validators.LengthValidator;
import com.elypia.commandler.validation.validators.LimitValidator;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Vaidates annotations associated with command paremeters to ensure
 * they are within the bounds that is specified relative to the user
 * performing the command.
 */

public class Validator {

    private Map<Class<? extends Annotation>, IValidator> validators;

    public Validator() {
        this(true);
    }

    public Validator(boolean auto) {
        validators = new HashMap<>();

        if (auto) {
            registerValidator(Length.class, new LengthValidator());
            registerValidator(Limit.class, new LimitValidator());
        }
    }

    public void registerValidator(Class<? extends Annotation> clazz, IValidator validator) {
        if (validators.keySet().contains(clazz))
            throw new IllegalArgumentException("Validator for this type of annotation has already been registered.");

        validators.put(clazz, validator);
    }

    public void validate(MessageEvent event, Method method, Object[] args) throws IllegalArgumentException, IllegalAccessException {
        MetaCommand command = MetaCommand.of(method);
        List<MetaParam> params = command.getParams();

        for (int i = 0; i < params.size(); i++) {
            MetaParam parameter = params.get(i);
            Annotation[] annotations = parameter.getAnnotations();

            for (int ii = 0; ii < annotations.length; ii++) {
                Annotation a = annotations[ii];
                Class<?> clazz = a.annotationType();
                IValidator validator = validators.get(clazz);

                if (validator != null)
                    validator.validate(args[i].getClass().cast(args[i]), a, parameter);
            }
        }
    }
}

package com.elypia.commandler.validation;

import com.elypia.commandler.impl.CommandValidator;
import com.elypia.commandler.interfaces.ICommandEvent;

import javax.validation.*;
import java.lang.reflect.*;

public class CommandlerConstraintValidatorFactory implements ConstraintValidatorFactory {

    private CommandValidator validator;

    public CommandlerConstraintValidatorFactory(CommandValidator validator) {
        this.validator = validator;
    }

    @Override
    public <T extends ConstraintValidator<?, ?>> T getInstance(Class<T> key) {
        try {
            Constructor<?>[] constructors = key.getConstructors();

            for (Constructor<?> constructor : constructors) {
                int count = constructor.getParameterCount();

                if (count == 0)
                    return key.cast(constructor.newInstance());

                if (count == 1) {
                    if (ICommandEvent.class.isAssignableFrom(constructor.getParameterTypes()[0]))
                        return key.cast(constructor.newInstance(validator.event));
                }
            }
        } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }

        String format = "%s must have a no parameter or single parameter constructor of type %s only.";
        throw new IllegalStateException(String.format(format, key.getName(), ICommandEvent.class.getName()));
    }

    @Override
    public void releaseInstance(ConstraintValidator<?, ?> instance) {
        // Do nothing
    }
}

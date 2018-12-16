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
                if (constructor.getParameterCount() == 1) {
                    if (ICommandEvent.class.isAssignableFrom(constructor.getParameterTypes()[0]))
                        return key.cast(constructor.newInstance(validator.event));
                }
            }

            return key.cast(key.getConstructor().newInstance());
        } catch (NoSuchMethodException | InstantiationException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }

        throw new IllegalStateException();
    }

    @Override
    public void releaseInstance(ConstraintValidator<?, ?> instance) {
        // Do nothing
    }
}

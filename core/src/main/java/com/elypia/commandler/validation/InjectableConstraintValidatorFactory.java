package com.elypia.commandler.validation;

import com.google.inject.Injector;

import javax.validation.*;

public class InjectableConstraintValidatorFactory implements ConstraintValidatorFactory {

    private Injector injector;

    public InjectableConstraintValidatorFactory(Injector injector) {
        this.injector = injector;
    }

    @Override
    public <T extends ConstraintValidator<?, ?>> T getInstance(Class<T> key) {
        return injector.getInstance(key);
    }

    @Override
    public void releaseInstance(ConstraintValidator<?, ?> instance) {
        // Do nothing
    }
}

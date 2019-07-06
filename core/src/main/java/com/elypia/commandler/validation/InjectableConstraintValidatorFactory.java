package com.elypia.commandler.validation;

import com.elypia.commandler.managers.InjectionManager;
import com.google.inject.*;

import javax.validation.*;

public class InjectableConstraintValidatorFactory implements ConstraintValidatorFactory {

    /** The {@link Guice} {@link Injector} to inject dependencies from. */
    private InjectionManager injectionManager;

    public InjectableConstraintValidatorFactory(InjectionManager injectionManager) {
        this.injectionManager = injectionManager;
    }

    @Override
    public <T extends ConstraintValidator<?, ?>> T getInstance(Class<T> key) {
        return injectionManager.getInjector().getInstance(key);
    }

    @Override
    public void releaseInstance(ConstraintValidator<?, ?> instance) {
        // Do nothing
    }
}

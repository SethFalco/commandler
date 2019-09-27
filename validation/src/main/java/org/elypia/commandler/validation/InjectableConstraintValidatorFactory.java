/*
 * Copyright 2019-2019 Elypia CIC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.elypia.commandler.validation;

import com.google.inject.*;
import org.elypia.commandler.managers.InjectionManager;

import javax.validation.*;

/**
 * @author seth@elypia.org (Syed Shah)
 */
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

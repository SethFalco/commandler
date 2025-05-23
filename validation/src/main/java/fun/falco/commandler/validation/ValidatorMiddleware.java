/*
 * Copyright 2019-2025 Seth Falco and Commandler Contributors
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

package fun.falco.commandler.validation;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.apache.deltaspike.core.api.provider.BeanProvider;

import fun.falco.commandler.api.HandlerMiddleware;
import fun.falco.commandler.event.ActionEvent;
import fun.falco.commandler.producers.ParameterWrapper;

/**
 * Adds a validation step to verify the parameters adhere to all
 * defined {@link javax.validation.Constraint} validations.
 *
 * @author seth@falco.fun (Seth Falco)
 */
@RequestScoped
public class ValidatorMiddleware implements HandlerMiddleware {

    private final HibernateValidationManager validationService;

    /** The parameters used to call the method. */
    private final ParameterWrapper parameterWrapper;

    @Inject
    public ValidatorMiddleware(HibernateValidationManager validationManager, ParameterWrapper parameterWrapper) {
        this.validationService = validationManager;
        this.parameterWrapper = parameterWrapper;
    }

    @Override
    public <S, M> void onMiddleware(ActionEvent<S, M> event) {
        Class<?> controllerType = event.getMetaController().getControllerType();
        Object controller = BeanProvider.getContextualReference(controllerType);
        validationService.validate(event, controller, parameterWrapper.getParams());
    }
}

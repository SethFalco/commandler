/*
 * Copyright 2019-2020 Elypia CIC
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

import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.elypia.commandler.api.*;
import org.elypia.commandler.event.ActionEvent;
import org.elypia.commandler.producers.ParameterWrapper;
import org.slf4j.*;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

/**
 * Adds a validation step to verify the parameters adhere to all
 * defined {@link javax.validation.Constraint} validations.
 *
 * @author seth@elypia.org (Seth Falco)
 */
@RequestScoped
public class ValidatorMiddleware implements HandlerMiddleware {

    private static final Logger logger = LoggerFactory.getLogger(ValidatorMiddleware.class);

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
        Class<? extends Controller> controllerType = event.getMetaController().getControllerType();
        Controller controller = BeanProvider.getContextualReference(controllerType);
        validationService.validate(event, controller, parameterWrapper.getParams());
    }
}

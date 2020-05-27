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
import org.elypia.commandler.managers.AdapterManager;
import org.slf4j.*;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 * Adds a validation step to verify the parameters adhere to all
 * defined {@link javax.validation.Constraint} validations.
 *
 * @author seth@elypia.org (Seth Falco)
 */
@ApplicationScoped
public class ValidatorMiddleware implements HandlerMiddleware {

    private static final Logger logger = LoggerFactory.getLogger(ValidatorMiddleware.class);

    private final AdapterManager adapterManager;
    private final HibernateValidationManager validationService;

    @Inject
    public ValidatorMiddleware(HibernateValidationManager validationService, AdapterManager adapterManager) {
        this.adapterManager = adapterManager;
        this.validationService = validationService;
    }

    /**
     * TODO: We're parsing the parameters twice, we should probably include them in the event
     * @param event
     * @param <S>
     * @param <M>
     */
    @Override
    public <S, M> void onMiddleware(ActionEvent<S, M> event) {
        Class<? extends Controller> type = event.getMetaController().getControllerType();
        Controller controller = BeanProvider.getContextualReference(type);
        Object[] params = adapterManager.adaptEvent(event);
        validationService.validate(event, controller, params);
    }
}

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

import org.apache.deltaspike.core.api.exception.control.event.ExceptionToCatchEvent;
import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.elypia.commandler.*;
import org.elypia.commandler.api.*;
import org.elypia.commandler.event.ActionEvent;
import org.elypia.commandler.exceptions.misuse.AbstractMisuseException;
import org.elypia.commandler.managers.*;
import org.elypia.commandler.metadata.*;
import org.slf4j.*;

import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;

/**
 * This extends the {@link ActionHandler} and handles everything the same way
 * except with the validation logic included.
 *
 * @author seth@elypia.org (Seth Falco)
 */
@Priority(Integer.MAX_VALUE)
@ApplicationScoped
@Alternative
public class ValidatedActionHandler extends ActionHandler {

    private static final Logger logger = LoggerFactory.getLogger(ValidatedActionHandler.class);

    private final HibernateValidationManager validationService;

    @Inject
    public ValidatedActionHandler(
        BeanManager beanManager,
        DispatcherManager dispatcherManager,
        HeaderManager headerManager,
        AdapterManager adapterService,
        //        TestManager testService,
        MessengerManager messengerService,
        HibernateValidationManager validationService
    ) {
//        super(dispatcherManager, cdiInjector, adapterService, testService, exceptionService, messengerService);
        super(beanManager, dispatcherManager, headerManager, adapterService, messengerService);
        this.validationService = validationService;
    }

    /**
     * Receieve and handles the event.
     *
     * @param integration The name of the service that received this.
     * @param content The content of the message.
     * @return The response to this command, or null
     * if this wasn't a command at all.
     */
    @Override
    public <S, M> M onAction(Integration<S, M> integration, S source, M message, String content) {
        Request<S, M> request = new Request<>(integration, source, message, content);
        headerManager.bindHeaders(request);
        logger.debug("Received action request with content: {}", content);

        Object response;
        ActionEvent<S, M> event = null;

        try {
            event = dispatcherManager.dispatch(request);

            if (event == null)
                return null;

            MetaController module = event.getMetaController();
            Controller controller = BeanProvider.getContextualReference(module.getHandlerType());
            Object[] params = adapterService.adaptEvent(event);
            validationService.validate(event, controller, params);

//            if (testService.isFailing(controller))
//                throw new ModuleDisabledException(event);

            MetaCommand metaCommand = event.getMetaCommand();
            response = metaCommand.getMethod().invoke(controller, params);
        } catch (AbstractMisuseException ex) {
            logger.info("A misuse exception occured when handling a message; command panicked.");
            beanManager.getEvent().fire(new ExceptionToCatchEvent(ex));
            return null;
        } catch (Exception ex) {
            logger.error("An exception occured when handling a message.", ex);
            beanManager.getEvent().fire(new ExceptionToCatchEvent(ex));
            return null;
        }

        if (response == null)
            return null;

        return messengerService.provide(event, response, integration);
    }
}

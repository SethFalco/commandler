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

import org.elypia.commandler.ActionHandler;
import org.elypia.commandler.api.*;
import org.elypia.commandler.event.ActionEvent;
import org.elypia.commandler.exceptions.misuse.MisuseException;
import org.elypia.commandler.injection.InjectorService;
import org.elypia.commandler.managers.*;
import org.elypia.commandler.metadata.*;
import org.slf4j.*;

import javax.inject.*;

/**
 * This extends the {@link ActionHandler} and handles everything the same way
 * except with the validation logic included.
 *
 * @author seth@elypia.org (Seth Falco)
 */
@Singleton
public class ValidatedActionHandler extends ActionHandler {

    private static final Logger logger = LoggerFactory.getLogger(ValidatedActionHandler.class);

    private final HibernateValidationManager validationService;

    @Inject
    public ValidatedActionHandler(
        DispatcherManager dispatcherManager,
        InjectorService injectorService,
        AdapterManager adapterService,
//        TestManager testService,
        MisuseManager exceptionService,
        MessengerManager messengerService,
        HibernateValidationManager validationService
    ) {
//        super(dispatcherManager, injectorService, adapterService, testService, exceptionService, messengerService);
        super(dispatcherManager, injectorService, adapterService, exceptionService, messengerService);
        this.validationService = validationService;
    }

    /**
     * Receieve and handles the event.
     *
     * @param integration The name of the service that recieved this.
     * @param content The content of the message.
     * @return The response to this command, or null
     * if this wasn't a command at all.
     */
    @Override
    public <S, M> M onAction(Integration<S, M> integration, S source, M message, String content) {
        logger.debug("Message Received: {}", content);

        Object response;
        ActionEvent<S, M> event = null;

        try {
            event = dispatcherManager.dispatch(integration, source, message, content);

            if (event == null)
                throw new IllegalStateException("Dispatching completed without panicking, but event is null.");

            MetaController module = event.getMetaController();
            Controller controller = injectorService.getInstance(module.getHandlerType());
            Object[] params = adapterService.adaptEvent(event);
            validationService.validate(event, controller, params);

//            if (testService.isFailing(controller))
//                throw new ModuleDisabledException(event);

            MetaCommand metaCommand = event.getMetaCommand();
            response = metaCommand.getMethod().invoke(controller, params);
        } catch (MisuseException ex) {
            logger.info("A misuse exception occured when handling a message; command panicked.");
            response = misuseManager.handle(ex);
        } catch (Exception ex) {
            logger.error("An exception occured when handling a message.", ex);
            response = "Something has gone wrong!";
        }

        if (response == null)
            return null;

        return messengerService.provide(event, response, integration);
    }
}

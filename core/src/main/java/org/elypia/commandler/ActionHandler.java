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

package org.elypia.commandler;

import org.apache.deltaspike.core.api.exception.control.event.ExceptionToCatchEvent;
import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.elypia.commandler.api.*;
import org.elypia.commandler.event.*;
import org.elypia.commandler.exceptions.misuse.AbstractMisuseException;
import org.elypia.commandler.managers.*;
import org.elypia.commandler.metadata.*;
import org.slf4j.*;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;

/**
 * The {@link ActionHandler} is what ultimiately handles all events
 * that come through Commandler regardless of service.
 *
 * This will iterate the registered {@link Dispatcher}s that report
 * an {@link Action} as {@link Dispatcher#isValid(Request)}
 * until one returns an object to respond to the user.
 *
 * @author seth@elypia.org (Seth Falco)
 */
@ApplicationScoped
@Alternative
public class ActionHandler implements ActionListener {

    private Logger logger = LoggerFactory.getLogger(ActionHandler.class);

    protected BeanManager beanManager;

    protected DispatcherManager dispatcherManager;
    protected HeaderManager headerManager;
    protected AdapterManager adapterService;
//    protected TestManager testService;
    protected MessengerManager messengerService;

    @Inject
    public ActionHandler(
        BeanManager beanManager,
        DispatcherManager dispatcherManager,
        HeaderManager headerManager,
        AdapterManager adapterService,
//        TestManager testService,
        MessengerManager messengerService
    ) {
        this.beanManager = beanManager;
        this.dispatcherManager = dispatcherManager;
        this.headerManager = headerManager;
        this.adapterService = adapterService;
//        this.testService = testService;
        this.messengerService = messengerService;
    }

    /**
     * Receieve and handles the event.
     *
     * @param integration The name of the service that receive this.
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
            response = "Something has gone wrong!";
        }

        if (response == null)
            return null;

        return messengerService.provide(event, response, integration);
    }
}

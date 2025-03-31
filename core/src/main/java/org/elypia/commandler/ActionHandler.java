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

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.control.ActivateRequestContext;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;

import org.apache.deltaspike.core.api.exception.control.event.ExceptionToCatchEvent;
import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.elypia.commandler.api.ActionListener;
import org.elypia.commandler.api.Dispatcher;
import org.elypia.commandler.api.HandlerMiddleware;
import org.elypia.commandler.api.Integration;
import org.elypia.commandler.event.Action;
import org.elypia.commandler.event.ActionEvent;
import org.elypia.commandler.event.Request;
import org.elypia.commandler.exceptions.misuse.AbstractMisuseException;
import org.elypia.commandler.managers.AdapterManager;
import org.elypia.commandler.managers.DispatcherManager;
import org.elypia.commandler.managers.HeaderManager;
import org.elypia.commandler.managers.MessengerManager;
import org.elypia.commandler.metadata.MetaCommand;
import org.elypia.commandler.metadata.MetaController;
import org.elypia.commandler.producers.RequestFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public class ActionHandler implements ActionListener {

    private Logger logger = LoggerFactory.getLogger(ActionHandler.class);

    private final BeanManager beanManager;
    private final DispatcherManager dispatcher;
    private final HeaderManager binder;
    private final AdapterManager adapter;
    private final MessengerManager messenger;
    private final Instance<? extends HandlerMiddleware> middlewares;

    @Inject
    public ActionHandler(BeanManager beanManager, DispatcherManager dispatcher, HeaderManager binder, AdapterManager adapter, MessengerManager messenger, Instance<HandlerMiddleware> middlewares) {
        this.beanManager = beanManager;
        this.dispatcher = dispatcher;
        this.binder = binder;
        this.adapter = adapter;
        this.messenger = messenger;
        this.middlewares = middlewares;
    }

    /**
     * Receive and handles the event.
     *
     * @param integration Name of the service that receive this.
     * @param content Content of the message.
     * @return
     *     Response to this command, or null if this wasn't a command at all.
     */
    @ActivateRequestContext
    @Override
    public <S, M> M onAction(Integration<S, M> integration, S source, M message, String content) {
        Request<S, M> request = new Request<>(integration, source, message, content);
        binder.bindHeaders(request);
        logger.debug("Received action request with content: {}", content);

        RequestFactory requestFactory = BeanProvider.getContextualReference(RequestFactory.class);
        requestFactory.setRequest(request);

        Object response = null;
        ActionEvent<S, M> event = null;

        try {
            event = dispatcher.dispatch(request);

            if (event == null) {
                return null;
            }

            requestFactory.setEvent(event);

            MetaController module = event.getMetaController();
            Object controller = BeanProvider.getContextualReference(module.getControllerType());
            Object[] params = adapter.adaptEvent(event);
            requestFactory.setParams(params);

            for (HandlerMiddleware middleware : middlewares) {
                middleware.onMiddleware(event);
            }

            MetaCommand metaCommand = event.getMetaCommand();
            response = metaCommand.getMethod().invoke(controller, params);
        } catch (AbstractMisuseException ex) {
            logger.info("A misuse exception occurred when handling a message; command panicked.");
            beanManager.getEvent().fire(new ExceptionToCatchEvent(ex));
        } catch (Exception ex) {
            logger.error("An uncaught exception occurred while handling a request.");
            beanManager.getEvent().fire(new ExceptionToCatchEvent(ex));
        }

        if (response == null) {
            return null;
        }

        return messenger.provide(event, response);
    }
}

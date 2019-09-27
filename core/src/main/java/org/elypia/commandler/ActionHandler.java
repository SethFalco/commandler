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

package org.elypia.commandler;

import org.elypia.commandler.api.*;
import org.elypia.commandler.event.*;
import org.elypia.commandler.exceptions.ModuleDisabledException;
import org.elypia.commandler.managers.*;
import org.elypia.commandler.metadata.*;

import javax.inject.*;

/**
 * The {@link ActionHandler} is what ultimiately handles all events
 * that come through Commandler regardless of service.
 *
 * This will iterate the registered {@link Dispatcher}s that report
 * an {@link Action} as {@link Dispatcher#isValid(Object, String)}
 * until one returns an object to respond to the user.
 *
 * @author seth@elypia.org (Syed Shah)
 */
@Singleton
public class ActionHandler implements ActionListener {

    protected DispatcherManager dispatcherManager;
    protected InjectionManager injectionService;
    protected AdapterManager adapterService;
    protected TestManager testService;
    protected ExceptionManager exceptionService;
    protected MessengerManager messengerService;

    @Inject
    public ActionHandler(
        DispatcherManager dispatcherManager,
        InjectionManager injectionService,
        AdapterManager adapterService,
        TestManager testService,
        ExceptionManager exceptionService,
        MessengerManager messengerService
    ) {
        this.dispatcherManager = dispatcherManager;
        this.injectionService = injectionService;
        this.adapterService = adapterService;
        this.testService = testService;
        this.exceptionService = exceptionService;
        this.messengerService = messengerService;
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
    public <S, M> M onAction(Integration<S, M> integration, S source, String content) {
        Object response;
        ActionEvent<S, M> event = null;

        try {
            event = dispatcherManager.dispatch(integration, source, content);
            MetaController module = event.getMetaController();
            Controller controller = injectionService.getInjector().getInstance(module.getHandlerType());
            Object[] params = adapterService.adaptEvent(event);

            if (testService.isFailing(controller))
                throw new ModuleDisabledException(event);

            MetaCommand metaCommand = event.getMetaCommand();
            response = metaCommand.getMethod().invoke(controller, params);
        } catch (Exception ex) {
            response = exceptionService.handle(ex);
        }

        if (response == null)
            return null;

        return messengerService.provide(event, response, integration);
    }
}

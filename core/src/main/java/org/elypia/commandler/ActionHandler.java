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
import org.elypia.commandler.exceptions.misuse.MisuseException;
import org.elypia.commandler.injection.InjectorService;
import org.elypia.commandler.managers.*;
import org.elypia.commandler.metadata.*;
import org.slf4j.*;

import javax.inject.*;

/**
 * The {@link ActionHandler} is what ultimiately handles all events
 * that come through Commandler regardless of service.
 *
 * This will iterate the registered {@link Dispatcher}s that report
 * an {@link Action} as {@link Dispatcher#isValid(Object, String)}
 * until one returns an object to respond to the user.
 *
 * @author seth@elypia.org (Seth Falco)
 */
@Singleton
public class ActionHandler implements ActionListener {

    private Logger logger = LoggerFactory.getLogger(ActionHandler.class);

    protected DispatcherManager dispatcherManager;
    protected InjectorService injectorService;
    protected AdapterManager adapterService;
//    protected TestManager testService;
    protected MisuseManager misuseManager;
    protected MessengerManager messengerService;

    @Inject
    public ActionHandler(
        DispatcherManager dispatcherManager,
        InjectorService injectorService,
        AdapterManager adapterService,
//        TestManager testService,
        MisuseManager misuseManager,
        MessengerManager messengerService
    ) {
        this.dispatcherManager = dispatcherManager;
        this.injectorService = injectorService;
        this.adapterService = adapterService;
//        this.testService = testService;
        this.misuseManager = misuseManager;
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

package com.elypia.commandler;

import com.elypia.commandler.api.*;
import com.elypia.commandler.event.*;
import com.elypia.commandler.exceptions.ModuleDisabledException;
import com.elypia.commandler.managers.*;
import com.elypia.commandler.metadata.*;

import javax.inject.Singleton;

/**
 * The {@link ActionHandler} is what ultimiately handles all events
 * that come through Commandler regardless of service.
 *
 * This will iterate the registered {@link Dispatcher}s that report
 * an {@link Action} as {@link Dispatcher#isValid(Object, String)}
 * until one returns an object to respond to the user.
 */
@Singleton
public class ActionHandler implements ActionListener {

    protected DispatcherManager dispatcherManager;
    protected InjectionManager injectionService;
    protected AdapterManager adapterService;
    protected TestManager testService;
    protected ExceptionManager exceptionService;
    protected MessengerManager messengerService;

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

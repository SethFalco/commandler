package org.elypia.commandler.validation;

import org.elypia.commandler.ActionHandler;
import org.elypia.commandler.api.*;
import org.elypia.commandler.event.*;
import org.elypia.commandler.exceptions.ModuleDisabledException;
import org.elypia.commandler.managers.*;
import org.elypia.commandler.metadata.*;

import javax.inject.*;

/**
 * This extends the {@link ActionHandler} and handles everything the same way
 * except with the validation logic included to ensure
 */
@Singleton
public class ValidatedActionHandler extends ActionHandler {

    protected HibernateValidationManager validationService;

    @Inject
    public ValidatedActionHandler(
        DispatcherManager dispatcherManager,
        InjectionManager injectionService,
        AdapterManager adapterService,
        TestManager testService,
        ExceptionManager exceptionService,
        MessengerManager messengerService,
        HibernateValidationManager validationService
    ) {
        super(dispatcherManager, injectionService, adapterService, testService, exceptionService, messengerService);
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
    public <S, M> M onAction(Integration<S, M> integration, S source, String content) {
        Object response;
        ActionEvent<S, M> event = null;

        try {
            event = dispatcherManager.dispatch(integration, source, content);
            Action input = event.getAction();
            MetaController module = event.getMetaController();
            Controller controller = injectionService.getInjector().getInstance(module.getHandlerType());
            Object[] params = adapterService.adaptEvent(event);
            validationService.validate(event, controller, params);

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

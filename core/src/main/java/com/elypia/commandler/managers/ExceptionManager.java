package com.elypia.commandler.managers;

import com.elypia.commandler.api.ExceptionHandler;
import com.elypia.commandler.metadata.MetaCommand;

import java.util.*;

/**
 * Whenever exceptions occur, this could be logical exceptions in the application,
 * internal Commandler exceptions due to a bug, or exception thrown by a
 * {@link MetaCommand} or Manager class due to some kind of user misuse.
 *
 * This class will handle the exceptions produced in runtime and report the
 * appropriate message to users. If a type of {@link Exception} is not
 * handled, then no message it sent to users.
 */
public class ExceptionManager {

    private Collection<ExceptionHandler> handlers;

    public ExceptionManager() {
        this(new ArrayList<>());
    }

    public ExceptionManager(ExceptionHandler... handlers) {
        this(new ArrayList<>(List.of(handlers)));
    }

    public ExceptionManager(Collection<ExceptionHandler> handlers) {
        this.handlers = handlers;
    }

    /**
     * @param ex The exception that needs to be handled.
     * @param <X> The type of exception that occured.
     * @return The message to send due to this exception,
     * or null if there is no message to be sent.
     */
    public <X extends Exception> Object handle(X ex) {
        for (ExceptionHandler handler : handlers) {
            Object o = handler.handle(ex);

            if (o != null)
                return o;
        }

        return null;
    }
}

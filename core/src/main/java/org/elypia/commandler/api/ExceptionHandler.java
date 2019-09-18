package org.elypia.commandler.api;

/**
 * This could handle a single exception, or route the
 * exeception to a method to handle that particular exception.
 */
@FunctionalInterface
public interface ExceptionHandler {

    /**
     * Handle the exception that occured if relevent
     * for this {@link ExceptionHandler} else throw nothing and
     * return to move to the next handler.
     *
     * @param ex The exception that occured.
     * @param <X> The type of exception that occured.
     * @return The object or message to send due to this exception.
     */
    <X extends Exception> Object handle(X ex);
}

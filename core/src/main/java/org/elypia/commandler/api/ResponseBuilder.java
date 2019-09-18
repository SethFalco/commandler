package org.elypia.commandler.api;

import org.elypia.commandler.event.ActionEvent;

/**
 * This should implement the default load method.
 * The default method should be the standard or minimal way
 * to send messages in a chat channel, usually raw text.
 * Platform specific implementations may append more
 * methods according to the different types of messages.
 *
 * @param <O> The object convert into a message.
 * @param <M> The message we're returning.
 */
public interface ResponseBuilder<O, M> {

    /**
     * The default load method, this adapters should be the default
     * way to send an {@link O object} of this type as a message.
     * Platform specific implementations may implement more load
     * methods for the diffenet message formats, if so this load method
     * should return the basic {@link M message} that requires minimal
     * permissions.
     *
     * @param output The output from the {@link Adapter} when parsing the input.
     * @return The message to response to the user.
     */
    M provide(ActionEvent<?, M> event, O output);
}

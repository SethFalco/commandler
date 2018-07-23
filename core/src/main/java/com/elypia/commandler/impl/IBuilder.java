package com.elypia.commandler.impl;

/**
 * This should implement the default build method.
 * The default method should be the standard or minimal way
 * to send messages in a chat channel, usually raw text.
 * Platform specific implementations may append more
 * methods according to the different types of messages.
 *
 * @param <O> The object to build, this should be the ouput from a {@link IParser}.
 * @param <M> The message we're returning.
 */
public interface IBuilder<CE extends ICommandEvent, O, M> {
    M build(CE event, O output);
}

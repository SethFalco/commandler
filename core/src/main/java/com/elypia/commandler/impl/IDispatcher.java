package com.elypia.commandler.impl;

import com.elypia.commandler.Commandler;
import com.elypia.commandler.CommandEvent;

/**
 * The {@link IDispatcher} is the event handler, and should recieve
 * all events from the client.
 *
 * @param <C> The type of the client we're integrating with.
 * @param <E> The type of event we're expecting to process.
 * @param <M> The data type the client expects when replying to a message.
 */

public interface IDispatcher<C, E, M> {

    /**
     * This should just overload the main {@link #processEvent(Object)} by passing
     * over the contents of the message. This is because internally there are
     * methods such as {@link CommandEvent#trigger(String)} and
     * {@link Commandler#trigger(Object, String)} which can spawn commands using
     * a different content using the same event.
     *
     * @param event The event spawned by the client.
     * @return The message that was sent to the client.
     */

    M processEvent(E event);

    /**
     * This should process the event.
     *
     * @param event The event spawned by the client.
     * @param content The content of the message to parse.
     * @return The message that was sent to the client.
     */

    M processEvent(E event, String content);
}

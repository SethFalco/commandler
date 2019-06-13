package com.elypia.commandler.interfaces;

import com.elypia.commandler.Commandler;

/**
 * The {@link CommandlerEvent} is the event object produced and used
 * by {@link Commandler} using the the {@link S event} spawned from the
 * client. This should contain convnience methods for the event
 * as well as manage any platform specific implementations.
 *
 * @param <S> The event that the client spawns.
 */
public interface CommandlerEvent<S> {
    S source();
}

package com.elypia.commandler.interfaces;

import com.elypia.commandler.CommandlerEvent;

/**
 * The service controllers provides a set of common methods that may be
 * required by respective services such as sending, editing or deleting messages.
 */
public interface ServiceController<E, M> {

    M receive(E event);

    void send(CommandlerEvent<E> event, M message);

    Class<?>[] getMessageTypes();

    default void edit(CommandlerEvent<E> event, M message) {
        throw new UnsupportedOperationException();
    }

    default void delete(CommandlerEvent<E> event, M message) {
        throw new UnsupportedOperationException();
    }
}

package org.elypia.commandler.dispatchers;

import org.elypia.commandler.api.*;
import org.elypia.commandler.event.ActionEvent;

public class MatchDispatcher implements Dispatcher {

    /**
     * Any message could match a potential regular expression.
     * As a result all messages are valid Match commands.
     *
     * @param event The source event that called this command.
     * @param content The content of the command.
     * @return If this is a valid command or not.
     */
    @Override
    public boolean isValid(Object event, String content) {
        return true;
    }

    @Override
    public <S, M> ActionEvent<S, M> parse(Integration<S, M> controller, S source, String content) {
        return null;
    }
}

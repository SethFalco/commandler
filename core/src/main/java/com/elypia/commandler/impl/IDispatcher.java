package com.elypia.commandler.impl;

import com.elypia.commandler.*;
import com.elypia.commandler.metadata.MetaCommand;

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
     * This should just overload the main {@link #processEvent(Object, String)} by passing
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
     * @param source The event spawned by the client.
     * @param content The content of the message to parse.
     * @return The message that was sent to the client.
     */
    default M processEvent(E source, String content){
        CommandEvent<C, E, M> event = getConfiler().processEvent(getCommandler(), source, content);
        CommandInput<C, E, M> input = event.getInput();

        if (!getCommandler().getRoots().containsKey(input.getModule().toLowerCase()) || !input.normalize(event))
            return event.getError();

        MetaCommand<C, E, M> metaCommand = event.getInput().getMetaCommand();

        if (!getCommandler().getValidator().validateCommand(event, metaCommand))
            return event.getError();

        Object[] params = getCommandler().getParser().processEvent(event, metaCommand);

        if (params == null || !getCommandler().getValidator().validateParams(event, metaCommand, params))
            return event.getError();

        if (!input.getCommand().equalsIgnoreCase("help") && !input.getMetaModule().getHandler().isEnabled()) {
            event.invalidate(getConfiler().getMisuseListener().onModuleDisabled(event));
            return event.getError();
        }

        try {
            Object message = metaCommand.getMethod().invoke(metaCommand.getHandler(), params);

            if (message != null)
                return event.reply(message);
        } catch (Exception ex) {
            event.invalidate(getConfiler().getMisuseListener().onExceptionFailure(ex));
            return event.getError();
        }

        return null;
    }

    Commandler<C, E, M> getCommandler();

    IConfiler<C, E, M> getConfiler();
}

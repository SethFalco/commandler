package com.elypia.commandler.impl;

import com.elypia.commandler.*;
import com.elypia.commandler.metadata.CommandData;

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
     * This should process the event.
     *
     * @param source The event spawned by the client.
     * @param content The content of the message to parse.
     * @return The message that was sent to the client.
     */
    default M processEvent(E source, String content, boolean send) {
        CommandEvent<C, E, M> event = getConfiler().processEvent(getCommandler(), source, content);

        if (event == null) // ? If it didn't have the command format
            return null;

        CommandInput<C, E, M> input = event.getInput();

        if (!getCommandler().getRoots().containsKey(input.getModule().toLowerCase()))
            return event.reply(getConfiler().getMisuseListener().onModuleNotFound(content));

        if (!input.normalize(event))
            return event.getError();

        CommandData<C, E, M> commandData = event.getInput().getCommandData();

        if (!getCommandler().getCommandValidator().validateCommand(event, commandData))
            return event.getError();

        Object[] params = getCommandler().getParser().processEvent(event, commandData);

        if (params == null || !getCommandler().getCommandValidator().validateParams(event, params))
            return event.getError();

        if (!input.getCommand().equalsIgnoreCase("help") && !input.getModuleData().getHandler().isEnabled()) {
            event.invalidate(getConfiler().getMisuseListener().onModuleDisabled(event));
            return event.getError();
        }

        try {
            Object response = commandData.getMethod().invoke(commandData.getHandler(), params);

            if (response != null && send)
                return event.reply(response);
        } catch (Exception ex) {
            event.invalidate(getConfiler().getMisuseListener().onException(ex));
            return event.getError();
        }

        return null;
    }

    Commandler<C, E, M> getCommandler();

    IConfiler<C, E, M> getConfiler();

    C getClient();
}

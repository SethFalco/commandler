package com.elypia.commandler;

import com.elypia.commandler.metadata.CommandData;

/**
 * The {@link Dispatcher} is the event handler, and should recieve
 * all events from the client.
 *
 * @param <C> The type of the client we're integrating with.
 * @param <E> The type of event we're expecting to process.
 * @param <M> The data type the client expects when replying to a message.
 */
public class Dispatcher<C, E, M> {

    /**
     * The {@link Commandler} instance which is what gives access to all
     * registers to add modules, parsers, senders, and validators.
     */
    protected Commandler<C, E, M> commandler;

    protected C client;

    protected Dispatcher(Commandler<C, E, M> commandler) {
        this.commandler = commandler;
        client = commandler.getClient();
    }

    /**
     * This should process the event.
     *
     * @param source The event spawned by the client.
     * @param content The content of the message to parse.
     * @return The message that was sent to the client.
     */
    public M processEvent(E source, String content, boolean send) {
        CommandEvent<C, E, M> event = getConfiler().processEvent(getCommandler(), source, content);

        if (event == null)
            return null;

        CommandInput<C, E, M> input = event.getInput();

        if (!getCommandler().getRoots().containsKey(input.getModule().toLowerCase()))
            return event.reply(commandler.getMisuseListener().onModuleNotFound(content));

        if (!input.normalize(event))
            return event.getError();

        CommandData commandData = event.getInput().getCommandData();

        if (!getCommandler().getCommandValidator().validateCommand(event, commandData))
            return event.getError();

        Object[] params = getCommandler().getParser().processEvent(event, commandData);

        if (params == null || !getCommandler().getCommandValidator().validateParams(event, params))
            return event.getError();

        if (!input.getCommand().equalsIgnoreCase("help") && !input.getModuleData().getHandler().isEnabled()) {
            event.invalidate(commandler.getMisuseListener().onModuleDisabled(event));
            return event.getError();
        }

        try {
            Object response = commandData.getMethod().invoke(commandData.getHandler(), params);

            if (response != null && send)
                return event.reply(response);
        } catch (Exception ex) {
            event.invalidate(commandler.getMisuseListener().onException(ex));
            return event.getError();
        }

        return null;
    }

    public Commandler<C, E, M> getCommandler() {
        return commandler;
    }

    public C getClient() {
        return client;
    }
}

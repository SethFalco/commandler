package com.elypia.commandler;

import com.elypia.commandler.impl.*;
import com.elypia.commandler.metadata.*;

/**
 * An abstract implementation for the {@link ICommandEvent}.
 * This should be extended for any platform specific implementations.
 *
 * @param <C> The client we're integrating with.
 * @param <E> The type of event we're processing.
 * @param <M> The type of message we send and receieve.
 */

public abstract class CommandEvent<C, E, M> implements ICommandEvent<C, E, M> {

    /**
     * The parent {@link Commandler} instance that spawned this {@link CommandEvent}.
     */

    protected Commandler<C, E, M> commandler;

    /**
     * A list of all input associated with this {@link CommandEvent}
     * as well as the respective {@link AbstractMetaCommand} and {@link MetaModule}.
     */

    protected CommandInput<C, E, M> input;

    /**
     * The {@link E event} that was spawned by the {@link C client}
     * and what was used to instantiate this {@link CommandEvent event}.
     */

    protected E event;

    protected M error;

    protected boolean invalidated;

    /**
     * Instantiate this event, this just sets the values of the parents
     * that spawned this event.
     *
     * @param commandler The parent {@link Commandler} which processed the event.
     * @param event The parent {@link E event} which was provided by the {@link C client}.
     */

    public CommandEvent(Commandler<C, E, M> commandler, CommandInput<C, E, M> input, E event) {
        this.commandler = commandler;
        this.event = event;
        this.input = input;
    }

    /**
     * This should be used internally where possible. This is the reply
     * method which is managed by {@link Commandler} seperate from any
     * reply or send message implementations provided by the client.
     * This allows {@link Commandler} to build and manage replies and perform
     * any pre or post actions for the platform we're integrating with.
     *
     * @param output The item to send to the {@link Builder} to process.
     * @param <O> The type of output, this can be anything so long as there
     *            is a registered {@link IBuilder} in the {@link Builder}.
     * @return The message that was built from the output.
     */

    @Override
    public abstract M reply(Object output);

    /**
     * Try delete the message that was sent in chat, if this isn't possible
     * for whatever reason, then do nothing.
     *
     * @return If a request was made to delete the message.
     */

    @Override
    public abstract boolean deleteMessage();

    /**
     * This indicates that the event has been invalidated. If the error
     * should be reported to the user then this should set {@link #error}
     * to a non-null value (the object to be sent to the user).
     *
     * @param reason Why the message was invalidated if the user should know.
     */

    @Override
    public void invalidate(Object reason) {
        invalidated = true;

        if (reason != null) {
            // ? Store the error in the event so we can return if from our dispatcher
            error = commandler.builder.build(this, reason);

            // ? Reply and let the user know what went wrong.
            reply(error);
        }
    }

    /**
     * @return The perant {@link Commandler} instance.
     */

    public Commandler<C, E, M> getCommandler() {
        return commandler;
    }

    /**
     * @return Quick access to the {@link IConfiler} this
     *         {@link #commandler} is using.
     */

    public IConfiler<C, E, M> getConfiler() {
        return commandler.getConfiler();
    }

    /**
     * @return Get the input parsed from this event.
     */

    public CommandInput<C, E, M> getInput() {
        return input;
    }

    public M getError() {
        return error;
    }

    /**
     * @return The {@link E event} that caused this event.
     */

    public E getSourceEvent() {
        return event;
    }
}

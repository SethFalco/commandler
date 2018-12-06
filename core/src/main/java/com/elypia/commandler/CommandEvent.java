package com.elypia.commandler;

import com.elypia.commandler.impl.ICommandEvent;
import com.elypia.commandler.metadata.*;

/**
 * An abstract implementation for the {@link ICommandEvent}.
 * This should be extended for any platform specific implementations.
 *
 * @param <C> The client we're integrating with.
 * @param <E> The type of event we're processing.
 * @param <M> The type of message we send and receieve.
 */
public class CommandEvent<C, E, M> implements ICommandEvent<C, E, M> {

    /**
     * The parent {@link Commandler} instance that spawned this {@link CommandEvent}.
     */
    protected Commandler<C, E, M> commandler;

    /**
     * A list of all input associated with this {@link CommandEvent}
     * as well as the respective {@link CommandData} and {@link ModuleData}.
     */
    protected CommandInput<C, E, M> input;

    protected C client;

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

        client = commandler.getClient();
    }

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

        // ? Store the error in the event so we can return if from our dispatcher
        if (reason != null)
            error = reply(reason);
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

    public C getClient() {
        return client;
    }

    /**
     * @return The {@link E event} that caused this event.
     */
    public E getSource() {
        return event;
    }
}

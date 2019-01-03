package com.elypia.commandler.impl;

import com.elypia.commandler.*;
import com.elypia.commandler.interfaces.*;

import java.util.Map;

/**
 * The {@link CommandEvent} is the event object produced and used
 * by {@link Commandler} using the the {@link S event} spawned from the
 * client. This should contain convnience methods for the event
 * as well as manage any platform specific implementations.
 *
 * @param <S> The event that the client spawns.
 * @param <M> The message type that users send and receieve.
 */
public abstract class CommandEvent<S, M> implements ICommandEvent<S, M> {

    protected Commandler<S, M> commandler;
    protected MessageBuilder<M> builder;
    protected IScripts<S> scripts;

    protected S source;

    protected CommandInput input;
    protected boolean isValid;
    protected String error;

    public CommandEvent(Commandler<S, M> commandler, S source, CommandInput input) {
        this.commandler = commandler;
        this.source = source;
        this.input = input;

        builder = commandler.getBuilder();
        scripts = commandler.getEngine();

        isValid = true;
    }

    public abstract <T> M send(String body, Map<String, T> params);

    /**
     * This indicates that the event has been invalidated. If the error
     * should be reported to the user then this should set {@link #error}
     * to a non-null value (the object to be sent to the user).
     *
     * @param reason Why the message was invalidated if the user should know.
     */
    @Override
    public void invalidate(String reason) {
        error = reason;
        isValid = false;
    }

    @Override
    public boolean isValid() {
        return isValid;
    }

    public Commandler<S, M> getCommandler() {
        return commandler;
    }

    public IScripts<S> getScripts() {
        return scripts;
    }

    public S getSource() {
        return source;
    }

    public CommandInput getInput() {
        return input;
    }

    public String getError() {
        return error;
    }

}

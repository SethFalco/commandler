package com.elypia.commandler;

import com.elypia.commandler.impl.*;

public abstract class Dispatcher<C, E, M> implements IDispatcher<C, E, M> {

    /**
     * The {@link Commandler} instance which is what gives access to all
     * registers to add modules, parsers, senders, and validators.
     */
    protected Commandler<C, E, M> commandler;

    protected IConfiler<C, E, M> confiler;

    protected C client;

    protected Dispatcher(Commandler<C, E, M> commandler) {
        this.commandler = commandler;
        confiler = commandler.getConfiler();
        client = commandler.getClient();
    }

    @Override
    public Commandler<C, E, M> getCommandler() {
        return commandler;
    }

    @Override
    public IConfiler<C, E, M> getConfiler() {
        return confiler;
    }

    public C getClient() {
        return client;
    }
}

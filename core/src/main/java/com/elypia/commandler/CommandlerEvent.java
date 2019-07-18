package com.elypia.commandler;

import com.elypia.commandler.interfaces.Controller;

// TODO: Make this into an interface so we can make different types of events.
public class CommandlerEvent<S, M> {

    private final Controller<M> controller;
    private final S source;
    private final Input input;

    public CommandlerEvent(Controller<M> controller, S source, Input input) {
        this.controller = controller;
        this.source = source;
        this.input = input;
    }

    public Controller<M> getController() {
        return controller;
    }

    public S getSource() {
        return source;
    }

    public Input getInput() {
        return input;
    }
}

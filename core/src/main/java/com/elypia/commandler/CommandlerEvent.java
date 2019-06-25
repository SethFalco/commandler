package com.elypia.commandler;

import com.elypia.commandler.interfaces.Controller;

public class CommandlerEvent<S> {

    private final Controller controller;
    private final S source;
    private final Input input;

    public CommandlerEvent(Controller controller, S source, Input input) {
        this.controller = controller;
        this.source = source;
        this.input = input;
    }

    public Controller getController() {
        return controller;
    }

    public S getSource() {
        return source;
    }

    public Input getInput() {
        return input;
    }
}

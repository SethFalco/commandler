package com.elypia.commandler;

import com.elypia.commandler.interfaces.ServiceController;

public class CommandlerEvent<S> {

    private final ServiceController<?, ?> service;
    private final S source;
    private final Input input;

    public CommandlerEvent(ServiceController service, S source, Input input) {
        this.service = service;
        this.source = source;
        this.input = input;
    }

    public ServiceController<?, ?> getService() {
        return service;
    }

    public S getSource() {
        return source;
    }

    public Input getInput() {
        return input;
    }
}

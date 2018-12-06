package com.elypia.commandler.impl.client;

public class StringEvent {

    private String string;

    public StringEvent(String string) {
        this.string = string;
    }

    public String getContent() {
        return string;
    }
}

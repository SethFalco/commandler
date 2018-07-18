package com.elypia.commandler.string.client;

import com.elypia.commandler.Commandler;

import java.util.*;

/**
 * The entirely of this project is made with the intent to
 * test {@link Commandler}, this is not a real implementation of it,
 * but rather an easy to test implementation so we can test
 * all common features any implementation will share.
 */

public class StringClient {

    private Collection<StringListener> listeners;

    public StringClient() {
        listeners = new ArrayList<>();
    }

    public void createString(String content) {
        StringEvent event = new StringEvent(content);
        listeners.forEach(listener -> listener.onStringEvent(event));
    }

    public void addListener(StringListener listener) {
        listeners.add(listener);
    }
}

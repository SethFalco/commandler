package org.elypia.commandler.api;

public abstract class AbstractIntegration<S, M> implements Integration<S, M> {

    private ActionListener listener;

    public M process(S source, String content) {
        return listener.onAction(this, source, content);
    }
}

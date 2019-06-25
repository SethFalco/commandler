package com.elypia.commandler.managers;

import com.elypia.commandler.interfaces.*;

import java.util.*;

/**
 * An ordered list of dispatchers to dispatch events
 * that are received appropriately.
 */
public class DispatchManager {

    private List<Dispatcher> dispatchers;

    public DispatchManager() {
        dispatchers = new ArrayList<>();
    }

    public Object dispatch(Controller controller, Object event, String content) {
        for (Dispatcher dispatcher : dispatchers) {
            if (!dispatcher.isValid(event, content))
                continue;

            return dispatcher.dispatch(controller, event, content);
        }

        return null;
    }

    public void addDispatchers(Dispatcher... dispatchers) {
        this.dispatchers.addAll(List.of(dispatchers));
    }
}

package com.elypia.commandler.managers;

import com.elypia.commandler.interfaces.*;
import org.slf4j.*;

import java.util.*;

/**
 * An ordered list of dispatchers to dispatch events
 * that are received appropriately.
 */
public class DispatcherManager {

    private static final Logger logger = LoggerFactory.getLogger(DispatcherManager.class);

    private List<Dispatcher> dispatchers;

    public DispatcherManager() {
        dispatchers = new ArrayList<>();
    }

    public Object dispatch(Controller controller, Object event, String content) {
        logger.debug("Dispatched event with content: {}", content);

        for (Dispatcher dispatcher : dispatchers) {
            if (!dispatcher.isValid(event, content))
                continue;

            logger.debug("Using dispatcher for event: {}", dispatcher);
            return dispatcher.dispatch(controller, event, content);
        }

        return null;
    }

    public void add(Dispatcher... dispatchers) {
        this.dispatchers.addAll(List.of(dispatchers));
    }
}

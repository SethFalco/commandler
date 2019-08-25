package com.elypia.commandler.metadata;

import java.util.*;

/**
 * Abstract class that represents a type with documentable elements
 * such as a {@link MetaController}, {@link MetaCommand}, and {@link MetaParam}.
 */
public abstract class MetaComponent {

    /** The display-friendly name of this item. */
    protected String name;

    /** A short helper description or message for what this item does. */
    protected String description;

    /** Properties that determine how this propery acts in runtime. */
    protected Properties properties;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Properties getProperties() {
        return properties;
    }

    public String getProperty(Class<?> type, String key) {
        return getProperty(type.getName() + "." + key);
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public Collection<String> getProperties(Class<?> type, String key) {
        return getProperties(type.getName() + "." + key);
    }

    public Collection<String> getProperties(String key) {
        return null;
    }
}

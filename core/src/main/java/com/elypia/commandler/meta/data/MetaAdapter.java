package com.elypia.commandler.meta.data;

import com.elypia.commandler.interfaces.Adapter;

import java.util.*;

/** The meta-data associated with a parameter adapters. */
public class MetaAdapter {

    /** The class of the adapters itself. */
    private Class<? extends Adapter> type;

    /** The compatible classes this adapters can adapt for. */
    private Collection<Class<?>> compatible;

    public MetaAdapter(Class<? extends Adapter> type, Class<?>... compatible) {
        this(type, List.of(compatible));
    }

    /**
     * @param type The class of the adapters itself.
     * @param compatible The compatible classes this adapters can adapt for.
     */
    public MetaAdapter(Class<? extends Adapter> type, Collection<Class<?>> compatible) {
        this.type = Objects.requireNonNull(type);
        this.compatible = Objects.requireNonNull(compatible);
    }

    public Class<? extends Adapter> getAdapterType() {
        return type;
    }

    public Collection<Class<?>> getCompatibleTypes() {
        return compatible;
    }
}

package com.elypia.commandler.metadata.data;

import com.elypia.commandler.interfaces.Adapter;

import java.util.*;

/** The meta-data associated with a parameter adapters. */
public class MetaAdapter {

    /** The class of the adapters itself. */
    private Class<? extends Adapter> clazz;

    /** The compatible classes this adapters can adapt for. */
    private Collection<Class<?>> compatible;

    public MetaAdapter(Class<? extends Adapter> clazz, Class<?>... compatible) {
        this(clazz, List.of(compatible));
    }

    /**
     * @param clazz The class of the adapters itself.
     * @param compatible The compatible classes this adapters can adapt for.
     */
    public MetaAdapter(Class<? extends Adapter> clazz, Collection<Class<?>> compatible) {
        this.clazz = Objects.requireNonNull(clazz);
        this.compatible = Objects.requireNonNull(compatible);
    }

    public Class<? extends Adapter> getAdapterClass() {
        return clazz;
    }

    public Collection<Class<?>> getCompatibleClasses() {
        return compatible;
    }
}

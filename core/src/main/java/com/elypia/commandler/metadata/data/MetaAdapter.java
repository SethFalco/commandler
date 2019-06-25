package com.elypia.commandler.metadata.data;

import com.elypia.commandler.interfaces.ParamAdapter;

import java.util.*;

/** The metadata-data associated with a parameter adapters. */
public class MetaAdapter {

    /** The class of the adapters itself. */
    private Class<? extends ParamAdapter> type;

    /** The compatible classes this adapters can adapt for. */
    private Collection<Class<?>> compatible;

    public MetaAdapter(Class<? extends ParamAdapter> type, Class<?>... compatible) {
        this(type, List.of(compatible));
    }

    /**
     * @param type The class of the adapters itself.
     * @param compatible The compatible classes this adapters can adapt for.
     */
    public MetaAdapter(Class<? extends ParamAdapter> type, Collection<Class<?>> compatible) {
        this.type = Objects.requireNonNull(type);
        this.compatible = Objects.requireNonNull(compatible);
    }

    public Class<? extends ParamAdapter> getAdapterType() {
        return type;
    }

    public Collection<Class<?>> getCompatibleTypes() {
        return compatible;
    }
}

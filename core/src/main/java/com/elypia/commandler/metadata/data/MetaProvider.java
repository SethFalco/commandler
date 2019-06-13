package com.elypia.commandler.metadata.data;

import com.elypia.commandler.interfaces.Provider;

import java.util.*;

public class MetaProvider {

    private Class<? extends Provider<?, ?>> clazz;
    private Collection<Class<?>> compatible;

    public MetaProvider(Class<? extends Provider<?, ?>> clazz, Collection<Class<?>> compatible) {
        this.clazz = Objects.requireNonNull(clazz);
        this.compatible = Objects.requireNonNull(compatible);
    }

    public Class<? extends Provider<?, ?>> getProviderClass() {
        return clazz;
    }

    public Collection<Class<?>> getCompatibleClasses() {
        return compatible;
    }
}

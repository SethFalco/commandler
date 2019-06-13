package com.elypia.commandler.metadata.builder;

import com.elypia.commandler.interfaces.Provider;
import com.elypia.commandler.metadata.ContextLoader;
import com.elypia.commandler.metadata.data.MetaProvider;

import java.util.Collection;

public class ProviderBuilder {

    private Class<? extends Provider<?, ?>> clazz;
    private Collection<Class<?>> compatible;

    public ProviderBuilder(Class<? extends Provider<?, ?>> clazz) {
        this.clazz = clazz;
    }

    public MetaProvider build(ContextLoader loader) {
        return new MetaProvider(clazz, compatible);
    }

    public Class<? extends Provider<?, ?>> getBuilderClass() {
        return clazz;
    }

    public Collection<Class<?>> getCompatible() {
        return compatible;
    }

    public ProviderBuilder setCompatible(Collection<Class<?>> compatible) {
        this.compatible = compatible;
        return this;
    }
}

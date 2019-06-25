package com.elypia.commandler.metadata.builder;

import com.elypia.commandler.interfaces.ResponseProvider;
import com.elypia.commandler.metadata.MetaBuilder;
import com.elypia.commandler.metadata.data.MetaProvider;

import java.util.*;

public class ProviderBuilder implements MetaBuilder<MetaProvider, ProviderBuilder> {

    private Class<? extends ResponseProvider<?, ?>> type;
    private Class<?> builds;
    private Collection<Class<?>> compatible;

    public ProviderBuilder(Class<? extends ResponseProvider<?, ?>> type) {
        this.type = type;
    }

    public Class<? extends ResponseProvider<?, ?>> getProviderType() {
        return type;
    }

    public Class<?> getBuildType() {
        return builds;
    }

    public ProviderBuilder setBuildType(Class<?> builds) {
        this.builds = builds;
        return this;
    }

    public Collection<Class<?>> getCompatibleTypes() {
        return compatible;
    }

    public ProviderBuilder setCompatibleTypes(Class<?>... compatible) {
        this.compatible = List.of(compatible);
        return this;
    }

    @Override
    public MetaProvider build() {
        return new MetaProvider(type, builds, compatible);
    }

    @Override
    public ProviderBuilder merge(ProviderBuilder builder) {
        return builder;
    }
}

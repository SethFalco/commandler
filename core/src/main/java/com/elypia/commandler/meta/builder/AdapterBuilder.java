package com.elypia.commandler.meta.builder;

import com.elypia.commandler.interfaces.Adapter;
import com.elypia.commandler.meta.MetaBuilder;
import com.elypia.commandler.meta.data.MetaAdapter;

import java.util.*;

public class AdapterBuilder implements MetaBuilder<MetaAdapter, AdapterBuilder> {

    private Class<? extends Adapter> type;
    private Collection<Class<?>> compatible;

    public AdapterBuilder(Class<? extends Adapter> type) {
        this.type = type;
    }

    public Class<? extends Adapter> getAdapterType() {
        return type;
    }

    public Collection<Class<?>> getCompatible() {
        return compatible;
    }

    public AdapterBuilder setCompatibleTypes(Class<?>... compatible) {
        this.compatible = List.of(compatible);
        return this;
    }

    @Override
    public MetaAdapter build() {
        return new MetaAdapter(type, compatible);
    }

    @Override
    public AdapterBuilder merge(AdapterBuilder builder) {
        if (builder.compatible == null)
            builder.compatible = compatible;

        return builder;
    }
}

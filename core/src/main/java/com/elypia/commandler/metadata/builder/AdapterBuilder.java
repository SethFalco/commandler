package com.elypia.commandler.metadata.builder;

import com.elypia.commandler.interfaces.ParamAdapter;
import com.elypia.commandler.metadata.MetaBuilder;
import com.elypia.commandler.metadata.data.MetaAdapter;

import java.util.*;

public class AdapterBuilder implements MetaBuilder<MetaAdapter, AdapterBuilder> {

    private Class<? extends ParamAdapter> type;
    private Collection<Class<?>> compatible;

    public AdapterBuilder(Class<? extends ParamAdapter> type) {
        this.type = type;
    }

    public Class<? extends ParamAdapter> getAdapterType() {
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

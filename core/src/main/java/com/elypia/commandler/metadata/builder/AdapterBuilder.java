package com.elypia.commandler.metadata.builder;

import com.elypia.commandler.interfaces.Adapter;
import com.elypia.commandler.metadata.ContextLoader;
import com.elypia.commandler.metadata.data.MetaAdapter;

import java.util.Collection;

public class AdapterBuilder {

    private Class<? extends Adapter> clazz;
    private Collection<Class<?>> compatible;

    public AdapterBuilder(Class<? extends Adapter> clazz) {
        this.clazz = clazz;
    }

    public MetaAdapter build(ContextLoader loader) {
        return new MetaAdapter(this.clazz, this.compatible);
    }

    public Class<? extends Adapter> getParserClass() {
        return clazz;
    }

    public Collection<Class<?>> getCompatible() {
        return compatible;
    }

    public AdapterBuilder setCompatible(Collection<Class<?>> compatible) {
        this.compatible = compatible;
        return this;
    }
}

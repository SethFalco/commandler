package com.elypia.commandler.metadata.builder;

import com.elypia.commandler.interfaces.Builder;
import com.elypia.commandler.metadata.ContextLoader;
import com.elypia.commandler.metadata.data.BuilderData;

import java.util.List;

public class BuilderBuilder {

    private Class<? extends Builder> clazz;
    private List<Class<?>> compatible;

    public BuilderBuilder(Class<? extends Builder> clazz) {
        this.clazz = clazz;
    }

    public BuilderData build(ContextLoader loader) {
        return new BuilderData(this);
    }

    public Class<? extends Builder> getBuilderClass() {
        return clazz;
    }

    public List<Class<?>> getCompatible() {
        return compatible;
    }

    public BuilderBuilder setCompatible(List<Class<?>> compatible) {
        this.compatible = compatible;
        return this;
    }
}

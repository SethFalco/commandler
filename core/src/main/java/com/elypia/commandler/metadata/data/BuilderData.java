package com.elypia.commandler.metadata.data;

import com.elypia.commandler.interfaces.Builder;
import com.elypia.commandler.metadata.builder.BuilderBuilder;

import java.util.List;

public class BuilderData {

    private Class<? extends Builder> clazz;
    private List<Class<?>> compatible;
    private Builder instance;

    public BuilderData(BuilderBuilder builder) {
        this.clazz = builder.getBuilderClass();
        this.compatible = builder.getCompatible();
    }

    public Class<? extends Builder> getBuilderClass() {
        return clazz;
    }

    public List<Class<?>> getCompatible() {
        return compatible;
    }

    public Builder getInstance() {
        return instance;
    }
}

package com.elypia.commandler.metadata.builder;

import com.elypia.commandler.interfaces.*;
import com.elypia.commandler.metadata.ContextLoader;
import com.elypia.commandler.metadata.data.MetaProvider;

import java.util.Collection;

public class ProviderBuilder {

    private Class<? extends ServiceController<?, ?>> serviceType;
    private Class<? extends ResponseProvider<?, ?>> clazz;
    private Collection<Class<?>> compatible;

    public ProviderBuilder(Class<? extends ResponseProvider<?, ?>> clazz) {
        this.clazz = clazz;
    }

    public MetaProvider build(ContextLoader loader) {
        return new MetaProvider(serviceType, clazz, compatible);
    }

    public Class<? extends ServiceController<?, ?>> getServiceType() {
        return serviceType;
    }

    public void setServiceType(Class<? extends ServiceController<?, ?>> serviceType) {
        this.serviceType = serviceType;
    }

    public Class<? extends ResponseProvider<?, ?>> getBuilderClass() {
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

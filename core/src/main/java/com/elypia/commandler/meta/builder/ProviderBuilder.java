package com.elypia.commandler.meta.builder;

import com.elypia.commandler.interfaces.*;
import com.elypia.commandler.meta.MetaBuilder;
import com.elypia.commandler.meta.data.MetaProvider;

import java.util.*;

public class ProviderBuilder implements MetaBuilder<MetaProvider, ProviderBuilder> {

    private Class<? extends ServiceController<?, ?>> serviceType;
    private Class<? extends ResponseProvider<?, ?>> type;
    private Collection<Class<?>> compatible;

    public ProviderBuilder(Class<? extends ResponseProvider<?, ?>> type) {
        this.type = type;
    }

    public Class<? extends ServiceController<?, ?>> getServiceType() {
        return serviceType;
    }

    public void setServiceType(Class<? extends ServiceController<?, ?>> serviceType) {
        this.serviceType = serviceType;
    }

    public Class<? extends ResponseProvider<?, ?>> getProviderType() {
        return type;
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
        return new MetaProvider(serviceType, type, compatible);
    }

    @Override
    public ProviderBuilder merge(ProviderBuilder builder) {


        return builder;
    }
}

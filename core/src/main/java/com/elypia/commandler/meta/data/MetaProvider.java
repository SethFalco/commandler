package com.elypia.commandler.meta.data;

import com.elypia.commandler.interfaces.*;

import java.util.*;

public class MetaProvider {

    /** What service is this a provider for. */
    private Class<? extends ServiceController<?, ?>> serviceType;

    /** The type of the provider itself. */
    private Class<? extends ResponseProvider<?, ?>> type;

    /** The types this provider is compatible for. */
    private Collection<Class<?>> compatible;

    public MetaProvider(Class<? extends ServiceController<?, ?>> serviceType, Class<? extends ResponseProvider<?, ?>> type, Collection<Class<?>> compatible) {
        this.serviceType = Objects.requireNonNull(serviceType);
        this.type = Objects.requireNonNull(type);
        this.compatible = Objects.requireNonNull(compatible);
    }

    public Class<? extends ServiceController<?, ?>> getServiceType() {
        return serviceType;
    }

    public Class<? extends ResponseProvider<?, ?>> getProviderType() {
        return type;
    }

    public Collection<Class<?>> getCompatibleClasses() {
        return compatible;
    }
}

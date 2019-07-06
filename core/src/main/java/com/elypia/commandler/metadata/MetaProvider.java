package com.elypia.commandler.metadata;

import com.elypia.commandler.interfaces.ResponseProvider;

import java.util.*;

public class MetaProvider {

    /** The type of the provider itself. */
    private Class<? extends ResponseProvider<?, ?>> type;

    /** The class this type provides. */
    private Class<?> builds;

    /** The types this provider is compatible for. */
    private Collection<Class<?>> compatible;

    public MetaProvider(Class<? extends ResponseProvider<?, ?>> type, Class<?> builds, Collection<Class<?>> compatible) {
        this.type = Objects.requireNonNull(type);
        this.builds = Objects.requireNonNull(builds);
        this.compatible = Objects.requireNonNull(compatible);
    }

    public Class<? extends ResponseProvider<?, ?>> getProviderType() {
        return type;
    }

    public Class<?> getBuildType() {
        return builds;
    }

    public Collection<Class<?>> getCompatibleTypes() {
        return compatible;
    }
}

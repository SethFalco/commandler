package org.elypia.commandler.metadata;

import org.elypia.commandler.api.ResponseBuilder;

import java.util.*;

public class MetaMessenger {

    /** The type of the provider itself. */
    private Class<? extends ResponseBuilder<?, ?>> type;

    /** The class this type provides. */
    private Class<?> builds;

    /** The types this provider is compatible for. */
    private Collection<Class<?>> compatible;

    public MetaMessenger(Class<? extends ResponseBuilder<?, ?>> type, Class<?> builds, Collection<Class<?>> compatible) {
        this.type = Objects.requireNonNull(type);
        this.builds = Objects.requireNonNull(builds);
        this.compatible = Objects.requireNonNull(compatible);
    }

    public Class<? extends ResponseBuilder<?, ?>> getProviderType() {
        return type;
    }

    public Class<?> getBuildType() {
        return builds;
    }

    public Collection<Class<?>> getCompatibleTypes() {
        return compatible;
    }
}

package com.elypia.commandler.interfaces;

import com.elypia.commandler.metadata.ContextLoader;

/**
 * Presets can be used to add a bulk of of implementation objects to
 * a {@link ContextLoader} at once. The primiarly use case for this is when depending
 * on libraries that provide default implementations of {@link Handler modules},
 * {@link ParamAdapter adapters}, or {@link ResponseProvider providers}.
 *
 * Presets must specify the loader to use to add the metadata, and must specify
 * classes directly, no package or package string is.
 */
public abstract class Preset {

    protected boolean modules;
    protected boolean adapters;
    protected boolean providers;

    public Preset(boolean modules, boolean adapters, boolean providers) {
        this.modules = modules;
        this.adapters = adapters;
        this.providers = providers;
    }

    public Class<?>[] getModules() {
        return null;
    }

    public Class<?>[] getAdapters() {
        return null;
    }

    public Class<?>[] getProviders() {
        return null;
    }
}

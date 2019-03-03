package com.elypia.commandler;

import com.elypia.commandler.impl.*;
import com.elypia.commandler.inject.ServiceProvider;
import com.elypia.commandler.interfaces.*;
import com.elypia.commandler.metadata.ContextLoader;

public class CommandlerBuilder<B extends CommandlerBuilder<?, S, M>, S, M> {

    protected String prefix;
    protected String website;
    protected ContextLoader context;
    protected MisuseListener<S, M> misuseHandler;
    protected LanguageAdapter<S> engine;
    protected ServiceProvider provider;

    public Commandler<S, M> build() {
        initializeDefaults();
        return new Commandler<>(this);
    }

    protected void initializeDefaults() {
        if (prefix == null)
            prefix = "!";

        if (context == null)
            context = new ContextLoader();

        if (misuseHandler == null)
            misuseHandler = new DefaultMisuseListener<>();

        if (engine == null)
            engine = new DefaultLanguageAdapter<>();
    }

    public String getPrefix() {
        return prefix;
    }

    public B setPrefix(String prefix) {
        this.prefix = prefix;
        return (B)this;
    }

    public String getWebsite() {
        return website;
    }

    public B setWebsite(String website) {
        this.website = website;
        return (B)this;
    }

    public ContextLoader getContextLoader() {
        return context;
    }

    public B setContextLoader(ContextLoader context) {
        this.context = context;
        return (B)this;
    }

    public MisuseListener<S, M> getMisuseHandler() {
        return misuseHandler;
    }

    public B setMisuseHandler(MisuseListener<S, M> misuseHandler) {
        this.misuseHandler = misuseHandler;
        return (B)this;
    }

    public LanguageAdapter<S> getEngine() {
        return engine;
    }

    public B setEngine(LanguageAdapter<S> engine) {
        this.engine = engine;
        return (B)this;
    }

    public ServiceProvider getServiceProvider() {
        return provider;
    }

    public B setServiceProvider(ServiceProvider provider) {
        this.provider = provider;
        return (B)this;
    }
}
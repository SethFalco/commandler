package com.elypia.commandler;

import com.elypia.commandler.impl.*;
import com.elypia.commandler.interfaces.*;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class Commandler<S, M> {

    protected String prefix;
    protected String website;
    protected ModulesContext context;
    protected IMisuseHandler<S, M> misuseHandler;
    protected IScripts<S> engine;

    protected ICommandProcessor<S, M> processor;
    protected CommandValidator validator;
    protected ParameterParser parser;
    protected MessageBuilder builder;

    protected Map<Class<? extends Handler<S, M>>, Handler<S, M>> handlers;

    protected Commandler(Builder<S, M> commandlerBuilder) {
        prefix = commandlerBuilder.prefix;
        context = commandlerBuilder.context;
        website = commandlerBuilder.website;
        misuseHandler = commandlerBuilder.misuseHandler;
        engine = commandlerBuilder.engine;

        processor = new CommandProcessor<>(this);
        validator = new CommandValidator(this);
        parser = new ParameterParser(misuseHandler);
        builder = new MessageBuilder();

        handlers = new HashMap<>();
    }

    public M execute(S event, String content, boolean send) {
        return processor.dispatch(event, content, send);
    }

    public void addInstance(Handler<S, M> handler) {

    }

    public Handler<S, M> getHandler(Class<? extends Handler<S, M>> handler) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        if (!handlers.containsKey(handler)) {
            Handler<S, M> handlerInstance = handler.getConstructor().newInstance();
            handlerInstance.init(this);

            handlers.put(handler, handlerInstance);
        }

        return handlers.get(handler);
    }

    public String getPrefix() {
        return prefix;
    }

    public String getWebsite() {
        return website;
    }

    public ModulesContext getContext() {
        return context;
    }

    public ICommandProcessor<S, M> getProcessor() {
        return processor;
    }

    public IMisuseHandler<S, M> getMisuseHandler() {
        return misuseHandler;
    }

    public IScripts<S> getEngine() {
        return engine;
    }

    public CommandValidator getValidator() {
        return validator;
    }

    public ParameterParser getParser() {
        return parser;
    }

    public MessageBuilder<M> getBuilder() {
        return builder;
    }

    public static class Builder<S, M> {

        protected String prefix;
        protected String website;
        protected ModulesContext context;
        protected IMisuseHandler<S, M> misuseHandler;
        protected IScripts<S> engine;

        public Commandler<S, M> build() {
            initializeDefaults();
            return new Commandler<>(this);
        }

        protected void initializeDefaults() {
            if (prefix == null)
                prefix = "!";

            if (context == null)
                context = new ModulesContext();

            if (misuseHandler == null)
                misuseHandler = new MisuseHandler<>();

            if (engine == null)
                engine = new Scripts<>();
        }

        public String getPrefix() {
            return prefix;
        }

        public Builder<S, M> setPrefix(String prefix) {
            this.prefix = prefix;
            return this;
        }

        public String getWebsite() {
            return website;
        }

        public Builder<S, M> setWebsite(String website) {
            this.website = website;
            return this;
        }

        public ModulesContext getContext() {
            return context;
        }

        public Builder<S, M> setContext(ModulesContext context) {
            this.context = context;
            return this;
        }

        public IMisuseHandler<S, M> getMisuseHandler() {
            return misuseHandler;
        }

        public Builder<S, M> setMisuseHandler(IMisuseHandler<S, M> misuseHandler) {
            this.misuseHandler = misuseHandler;
            return this;
        }

        public IScripts<S> getEngine() {
            return engine;
        }

        public Builder<S, M> setEngine(IScripts<S> engine) {
            this.engine = engine;
            return this;
        }
    }
}

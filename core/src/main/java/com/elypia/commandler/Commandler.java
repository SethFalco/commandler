package com.elypia.commandler;

import com.elypia.commandler.impl.*;
import com.elypia.commandler.interfaces.*;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class Commandler<C, E, M> {

    private String prefix;
    private String website;
    private ModulesContext context;
    private IMisuseHandler<C, E, M> misuseHandler;
    private LanguageEngine<E> engine;

    private ICommandProcessor<C, E, M> processor;
    private CommandValidator validator;
    private ParameterParser parser;
    private MessageBuilder builder;

    private Map<Class<? extends Handler<C, E, M>>, Handler<C, E, M>> handlers;

    protected Commandler(Builder<C, E, M> commandlerBuilder) {
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

    public M execute(E event, String content, boolean send) {
        return processor.dispatch(event, content, send);
    }

    public void addInstance(Handler<C, E, M> handler) {

    }

    public Handler<C, E, M> getHandler(Class<? extends Handler<C, E, M>> handler) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        if (!handlers.containsKey(handler)) {
            Handler<C, E, M> handlerInstance = handler.getConstructor().newInstance();
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

    public ICommandProcessor<C, E, M> getProcessor() {
        return processor;
    }

    public IMisuseHandler<C, E, M> getMisuseHandler() {
        return misuseHandler;
    }

    public LanguageEngine<E> getEngine() {
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

    public static class Builder<C, E, M> {

        private String prefix;
        private String website;
        private ModulesContext context;
        private IMisuseHandler<C, E, M> misuseHandler;
        private LanguageEngine<E> engine;

        public Commandler<C, E, M> build() {
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
                engine = new LanguageEngine<>();
        }

        public String getPrefix() {
            return prefix;
        }

        public Builder<C, E, M> setPrefix(String prefix) {
            this.prefix = prefix;
            return this;
        }

        public String getWebsite() {
            return website;
        }

        public Builder<C, E, M> setWebsite(String website) {
            this.website = website;
            return this;
        }

        public ModulesContext getContext() {
            return context;
        }

        public Builder<C, E, M> setContext(ModulesContext context) {
            this.context = context;
            return this;
        }

        public IMisuseHandler<C, E, M> getMisuseHandler() {
            return misuseHandler;
        }

        public Builder<C, E, M> setMisuseHandler(IMisuseHandler<C, E, M> misuseHandler) {
            this.misuseHandler = misuseHandler;
            return this;
        }

        public LanguageEngine<E> getEngine() {
            return engine;
        }

        public Builder<C, E, M> setEngine(LanguageEngine<E> engine) {
            this.engine = engine;
            return this;
        }
    }
}

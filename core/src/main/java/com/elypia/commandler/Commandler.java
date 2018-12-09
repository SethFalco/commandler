package com.elypia.commandler;

import com.elypia.commandler.impl.*;
import com.elypia.commandler.interfaces.*;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class Commandler<C, E, M> {

    private C client;

    private String prefix;
    private String website;
    private IMisuseHandler<C, E, M> misuseHandler;
    private LanguageEngine<E> engine;

    private ModulesContext context;
    private ICommandProcessor<C, E, M> processor;
    private CommandValidator validator;
    private ParameterParser parser;
    private MessageBuilder builder;

    private Map<Class<? extends Handler>, Handler<C, E, M>> handlers;

    private Commandler(Builder<C, E, M> commandlerBuilder) {
        client = commandlerBuilder.client;
        prefix = commandlerBuilder.prefix;
        website = commandlerBuilder.website;
        misuseHandler = commandlerBuilder.misuseHandler;
        engine = commandlerBuilder.engine;

        context = new ModulesContext(this);
        processor = new CommandProcessor<>(this);
        validator = new CommandValidator(this);
        parser = new ParameterParser(misuseHandler);
        builder = new MessageBuilder();

        handlers = new HashMap<>();
    }

    public M execute(E event, String content, boolean send) {
        return processor.dispatch(event, content, send);
    }

    public Handler<C, E, M> getHandler(Class<? extends Handler> handler) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        if (!handlers.containsKey(handler)) {
            Handler<C, E, M> handlerInstance = handler.getConstructor().newInstance();
            handlerInstance.init(this);

            handlers.put(handler, handlerInstance);
        }

        return handlers.get(handler);
    }

    public C getClient() {
        return client;
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

        private C client;

        private String prefix;
        private String website;
        private IMisuseHandler<C, E, M> misuseHandler;
        private LanguageEngine<E> engine;

        public Commandler<C, E, M> build() {
            initializeDefaults();
            return new Commandler<>(this);
        }

        private void initializeDefaults() {
            if (prefix == null)
                prefix = "!";

            if (misuseHandler == null)
                misuseHandler = new MisuseHandler<>();

            if (engine == null)
                engine = new LanguageEngine<>();
        }

        public C getClient() {
            return client;
        }

        public Builder<C, E, M> setClient(C client) {
            this.client = client;
            return this;
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

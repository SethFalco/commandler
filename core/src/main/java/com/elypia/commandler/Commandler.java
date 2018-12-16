package com.elypia.commandler;

import com.elypia.commandler.impl.*;
import com.elypia.commandler.interfaces.*;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class Commandler<E, M> {

    private String prefix;
    private String website;
    private ModulesContext context;
    private IMisuseHandler<E, M> misuseHandler;
    private LanguageEngine<E> engine;

    private ICommandProcessor<E, M> processor;
    private CommandValidator validator;
    private ParameterParser parser;
    private MessageBuilder builder;

    private Map<Class<? extends Handler<E, M>>, Handler<E, M>> handlers;

    protected Commandler(Builder<E, M> commandlerBuilder) {
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

    public void addInstance(Handler<E, M> handler) {

    }

    public Handler<E, M> getHandler(Class<? extends Handler<E, M>> handler) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        if (!handlers.containsKey(handler)) {
            Handler<E, M> handlerInstance = handler.getConstructor().newInstance();
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

    public ICommandProcessor<E, M> getProcessor() {
        return processor;
    }

    public IMisuseHandler<E, M> getMisuseHandler() {
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

    public static class Builder<E, M> {

        private String prefix;
        private String website;
        private ModulesContext context;
        private IMisuseHandler<E, M> misuseHandler;
        private LanguageEngine<E> engine;

        public Commandler<E, M> build() {
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

        public Builder<E, M> setPrefix(String prefix) {
            this.prefix = prefix;
            return this;
        }

        public String getWebsite() {
            return website;
        }

        public Builder<E, M> setWebsite(String website) {
            this.website = website;
            return this;
        }

        public ModulesContext getContext() {
            return context;
        }

        public Builder<E, M> setContext(ModulesContext context) {
            this.context = context;
            return this;
        }

        public IMisuseHandler<E, M> getMisuseHandler() {
            return misuseHandler;
        }

        public Builder<E, M> setMisuseHandler(IMisuseHandler<E, M> misuseHandler) {
            this.misuseHandler = misuseHandler;
            return this;
        }

        public LanguageEngine<E> getEngine() {
            return engine;
        }

        public Builder<E, M> setEngine(LanguageEngine<E> engine) {
            this.engine = engine;
            return this;
        }
    }
}

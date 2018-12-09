package com.elypia.commandler;

import com.elypia.commandler.impl.*;
import com.elypia.commandler.interfaces.*;

public class Commandler<C, E, M> {

    private C client;

    private String prefix;
    private String website;
    private ModulesContext context;
    private IMisuseHandler<C, E, M> misuseHandler;
    private LanguageEngine<E> engine;

    private ICommandProcessor<C, E, M> processor;
    private CommandValidator validator;
    private ParameterParser parser;
    private MessageBuilder builder;

    private Commandler(Builder<C, E, M> commandlerBuilder) {
        client = commandlerBuilder.client;
        prefix = commandlerBuilder.prefix;
        website = commandlerBuilder.website;
        context = commandlerBuilder.context;
        misuseHandler = commandlerBuilder.misuseHandler;
        engine = commandlerBuilder.engine;

        processor = new CommandProcessor<>(this);
        validator = new CommandValidator(this);
        parser = new ParameterParser(misuseHandler);
        builder = new MessageBuilder();
    }

    public M execute(E event, String content, boolean send) {
        return processor.dispatch(event, content, send);
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
        private ModulesContext context;
        private IMisuseHandler<C, E, M> misuseHandler;
        private LanguageEngine<E> engine;

        public Commandler<C, E, M> build() {
            initializeDefaults();
            return new Commandler<>(this);
        }

        private void initializeDefaults() {
            if (prefix == null)
                prefix = "!";

            if (context == null)
                context = new ModulesContext();

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

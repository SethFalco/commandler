package com.elypia.commandler;

import com.elypia.commandler.interfaces.*;
import org.slf4j.*;

public class CommandlerBuilder<C, E, M> {

    private static final Logger logger = LoggerFactory.getLogger(CommandlerBuilder.class);

    private ModulesContext context;

    private ICommandProcessor processor;

    private LanguageEngine<E> engine;

    private IMisuseHandler handler;

    private String prefix;

    private String website;

    public Commandler<C, E, M> build() {
        initConfig();
        initDefaults();

        return new Commandler<>(this);
    }

    /**
     * Any fields that have not been populated via code will
     * be populated via configuration if there is a configuration
     * file available.
     */
    private void initConfig() {

    }

    /**
     * Any fields that have still not been populated by code
     * or config will be given default values.
     */
    private void initDefaults() {
        if (context == null)
            context = new ModulesContext();

        if (engine == null)
            engine = new LanguageEngine<>();

        if (prefix == null)
            prefix = "!";
    }

    public ModulesContext getContext() {
        return context;
    }

    public CommandlerBuilder<C, E, M> setContext(ModulesContext context) {
        this.context = context;
        return this;
    }

    public ICommandProcessor getProcessor() {
        return processor;
    }

    public CommandlerBuilder<C, E, M> setProcessor(ICommandProcessor processor) {
        this.processor = processor;
        return this;
    }

    public LanguageEngine getEngine() {
        return engine;
    }

    public CommandlerBuilder<C, E, M> setEngine(LanguageEngine<E> engine) {
        this.engine = engine;
        return this;
    }

    public String getPrefix() {
        return prefix;
    }

    public CommandlerBuilder<C, E, M> setPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    public String getWebsite() {
        return website;
    }

    public CommandlerBuilder<C, E, M> setWebsite(String website) {
        this.website = website;
        return this;
    }
}

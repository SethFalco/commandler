package com.elypia.commandler;

import com.elypia.commandler.def.DefCommandProcessor;
import com.elypia.commandler.interfaces.*;
import com.elypia.commandler.metadata.*;
import com.elypia.commandler.testing.TestRunner;
import com.elypia.commandler.validation.CommandValidator;
import com.google.inject.Injector;

public class Commandler<S, M> {

    protected String prefix;
    protected String website;
    protected Context context;
    protected MisuseHandler misuseHandler;
    protected LanguageAdapter<S> engine;
    protected MessageProvider<M> builder;

    protected CommandProcessor<S, M> processor;
    protected CommandValidator validator;
    protected ParamAdapter parser;
    protected TestRunner runner;

    protected Injector injector;

    protected Commandler(
        String prefix,
        String website,
        ContextLoader contextLoader,
        MisuseHandler misuseHandler,
        LanguageAdapter<S> engine,
        Injector injector
    ) {
        this.injector = injector;
        this.context = contextLoader.load();
        this.misuseHandler = misuseHandler;
        this.engine = engine;

        this.prefix = prefix;
        this.website = website;
        this.processor = new DefCommandProcessor(this);
        this.validator = new CommandValidator(injector, this);
        this.runner = new TestRunner(this);

        this.parser = new ParamAdapter(injector, context.getParsers());
        this.builder = new MessageProvider<>(injector, context.getBuilders());
    }

    public M execute(S event, String content, boolean send) {
        return processor.dispatch(event, content, send);
    }

    public String getPrefix() {
        return prefix;
    }

    public String getWebsite() {
        return website;
    }

    public Context getContext() {
        return context;
    }

    public CommandProcessor<S, M> getProcessor() {
        return processor;
    }

    public MisuseHandler getMisuseHandler() {
        return misuseHandler;
    }

    public LanguageAdapter<S> getEngine() {
        return engine;
    }

    public CommandValidator getValidator() {
        return validator;
    }

    public ParamAdapter getParser() {
        return parser;
    }

    public MessageProvider<M> getBuilder() {
        return builder;
    }

    public TestRunner getTestRunner() {
        return runner;
    }

    public Injector getInjector() {
        return injector;
    }
}

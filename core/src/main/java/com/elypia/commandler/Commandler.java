package com.elypia.commandler;

import com.elypia.commandler.impl.DefaultCommandProcessor;
import com.elypia.commandler.inject.ServiceProvider;
import com.elypia.commandler.interfaces.*;
import com.elypia.commandler.metadata.*;
import com.elypia.commandler.testing.TestRunner;

public class Commandler<S, M> {

    protected String prefix;
    protected String website;
    protected Context context;
    protected MisuseListener<S, M> misuseHandler;
    protected LanguageAdapter<S> engine;
    protected ResponseBuilder<M> builder;

    protected CommandProcessor<S, M> processor;
    protected CommandValidator validator;
    protected ParameterParser parser;
    protected TestRunner runner;

    protected ServiceProvider provider;

    protected Commandler(CommandlerBuilder<?, S, M> commandlerBuilder) {
        provider = new ServiceProvider(this);
        context = commandlerBuilder.context.load();
        misuseHandler = commandlerBuilder.misuseHandler;
        engine = commandlerBuilder.engine;

        provider.createInjector();

        prefix = commandlerBuilder.prefix;
        website = commandlerBuilder.website;
        processor = new DefaultCommandProcessor(this);
        validator = new CommandValidator(this);
        runner = new TestRunner(this);

        parser = new ParameterParser(provider, context.getParsers());
        builder = new ResponseBuilder<>(provider, context.getBuilders());
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

    public MisuseListener<S, M> getMisuseHandler() {
        return misuseHandler;
    }

    public LanguageAdapter<S> getEngine() {
        return engine;
    }

    public CommandValidator getValidator() {
        return validator;
    }

    public ParameterParser getParser() {
        return parser;
    }

    public ResponseBuilder<M> getBuilder() {
        return builder;
    }

    public TestRunner getTestRunner() {
        return runner;
    }

    public ServiceProvider getServiceProvider() {
        return provider;
    }
}

package com.elypia.commandler;

import com.elypia.commandler.core.*;
import com.elypia.commandler.def.*;
import com.elypia.commandler.interfaces.*;
import com.elypia.commandler.meta.ContextLoader;
import com.elypia.commandler.testing.TestRunner;
import com.elypia.commandler.validation.CommandValidator;
import com.google.inject.*;

/**
 * Commandler brings all components together to process
 * command events to perform method calls and return
 * the result.
 */
public class Commandler {

    /** The collation of MetaData used to initialize this Commandler instance. */
    private Context context;

    /** Handler for errors, both end-user errors and exceptions. */
    private MisuseHandler misuseHandler;

    /** Obtain externally managed strings for internationalization. */
    private LanguageManager langManager;

    /** Take the result of a method and make it something sendable to the {@link ServiceController}. */
    private MessageProvider provider;

    private Dispatcher<?, ?> dispatcher;
    private CommandValidator validator;
    private ParamAdapter parser;
    private TestRunner runner;

    private Injector injector;

    /**
     * @param contextLoader The context loaders in order to find and initialize
     *                      Commandler and all special objects.
     */
    public Commandler(ContextLoader contextLoader) {
        this(contextLoader, new DefMisuseHandler());
    }

    public Commandler(ContextLoader contextLoader, MisuseHandler misuseHandler) {
        this(contextLoader, misuseHandler, new DefLanguageManager<>());
    }

    public Commandler(ContextLoader contextLoader, MisuseHandler misuseHandler, LanguageManager langManager) {
        this(contextLoader, misuseHandler, langManager, Guice.createInjector());
    }

    public Commandler(ContextLoader contextLoader, MisuseHandler misuseHandler, LanguageManager langManager, Injector injector) {
        this.injector = injector;
        this.context = contextLoader.load().build();
        this.misuseHandler = misuseHandler;
        this.langManager = langManager;

        this.dispatcher = new DefDispatcher(this);
        this.validator = new CommandValidator(injector, context);
        this.runner = new TestRunner(this);

        this.parser = new ParamAdapter(injector, context.getAdapters());
        this.provider = new MessageProvider(injector, context.getProviders());
    }

    public Context getContext() {
        return context;
    }

    public Dispatcher getDispatcher() {
        return dispatcher;
    }

    public MisuseHandler getMisuseHandler() {
        return misuseHandler;
    }

    public LanguageManager getEngine() {
        return langManager;
    }

    public CommandValidator getValidator() {
        return validator;
    }

    public ParamAdapter getParser() {
        return parser;
    }

    public MessageProvider getProvider() {
        return provider;
    }

    public TestRunner getTestRunner() {
        return runner;
    }

    public Injector getInjector() {
        return injector;
    }
}

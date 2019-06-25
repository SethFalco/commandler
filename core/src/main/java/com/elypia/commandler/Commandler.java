package com.elypia.commandler;

import com.elypia.commandler.dispatchers.CommandDispatcher;
import com.elypia.commandler.interfaces.*;
import com.elypia.commandler.managers.*;
import com.elypia.commandler.misuse.CommandMisuseListener;
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

    /** Take the result of a method and make it something sendable to the service. */
    private ProviderManager provider;

    private DispatchManager dispatch;
    private ValidationManager validator;
    private AdapterManager adapter;
    private TestManager runner;

    private Injector injector;

    /**
     * @param context The context loaders in order to find and initialize
     *                      Commandler and all special objects.
     */
    public Commandler(Context context) {
        this(context, new CommandMisuseListener());
    }

    public Commandler(Context context, MisuseHandler misuseHandler) {
        this(context, misuseHandler, new DefLanguageManager<>());
    }

    public Commandler(Context context, MisuseHandler misuseHandler, LanguageManager langManager) {
        this(context, misuseHandler, langManager, Guice.createInjector(new CommandlerModule()));
    }

    public Commandler(Context context, MisuseHandler misuseHandler, LanguageManager langManager, Injector injector) {
        this.injector = injector;
        this.context = context;
        this.misuseHandler = misuseHandler;
        this.langManager = langManager;

        this.dispatch = new DispatchManager();
        this.dispatch.addDispatchers(new CommandDispatcher(this, ">"));

        this.validator = new ValidationManager(injector, context);
        this.runner = new TestManager(this);

        this.adapter = new AdapterManager(injector, context.getAdapters());
        this.provider = new ProviderManager(injector, context.getProviders());
    }

    public Context getContext() {
        return context;
    }

    public DispatchManager getDispatchManager() {
        return dispatch;
    }

    public MisuseHandler getMisuseManager() {
        return misuseHandler;
    }

    public LanguageManager getLanguageManager() {
        return langManager;
    }

    public ValidationManager getValidationManager() {
        return validator;
    }

    public AdapterManager getAdapterManager() {
        return adapter;
    }

    public ProviderManager getProviderManager() {
        return provider;
    }

    public TestManager getTestManager() {
        return runner;
    }

    public Injector getInjector() {
        return injector;
    }
}

package com.elypia.commandler;

import com.elypia.commandler.interfaces.*;
import com.elypia.commandler.managers.*;
import com.elypia.commandler.misuse.CommandMisuseListener;
import com.google.inject.*;

import java.util.Objects;

/**
 * Commandler brings all components together to process
 * command events to perform method calls and return
 * the result.
 */
public class Commandler {

    /** The collation of MetaData used to initialize this Commandler instance. */
    private Context context;

    /** Handler for errors, both end-user errors and exceptions. */
    private MisuseHandler misuseManager;

    /** Obtain externally managed strings for internationalization. */
    private LanguageInterface languageManager;

    /** Take the result of a method and make it something sendable to the service. */
    private ResponseManager responseManager;

    private DispatcherManager dispatcherManager;
    private ValidationManager validationManager;
    private AdapterManager adapterManager;
    private TestManager testManager;

    private Injector injector;

    public Commandler(
        Context context,
        MisuseHandler misuseManager,
        LanguageInterface languageManager,
        ResponseManager responseManager,
        DispatcherManager dispatcherManager,
        ValidationManager validationManager,
        AdapterManager adapterManager,
        TestManager testManager,
        Injector injector
    ) {
        this.context = context;
        this.misuseManager = misuseManager;
        this.languageManager = languageManager;
        this.responseManager = responseManager;
        this.dispatcherManager = dispatcherManager;
        this.validationManager = validationManager;
        this.adapterManager = adapterManager;
        this.testManager = testManager;
        this.injector = injector;

        this.injector = injector.createChildInjector(new CommandlerModule(this));
    }

    public Context getContext() {
        return context;
    }

    public DispatcherManager getDispatcherManager() {
        return dispatcherManager;
    }

    public MisuseHandler getMisuseManager() {
        return misuseManager;
    }

    public LanguageInterface getLanguageManager() {
        return languageManager;
    }

    public ValidationManager getValidationManager() {
        return validationManager;
    }

    public AdapterManager getAdapterManager() {
        return adapterManager;
    }

    public ResponseManager getResponseManager() {
        return responseManager;
    }

    public TestManager getTestManager() {
        return testManager;
    }

    public Injector getInjector() {
        return injector;
    }

    public static class Builder {

        private Context context;
        private MisuseHandler misuseManager;
        private LanguageInterface languageManager;
        private ResponseManager responseManager;
        private DispatcherManager dispatcherManager;
        private ValidationManager validationManager;
        private AdapterManager adapterManager;
        private TestManager testManager;
        private Injector injector;

        public Builder(Context context) {
            this.context = Objects.requireNonNull(context);
        }

        public Commandler build() {
            if (misuseManager == null)
                this.misuseManager = new CommandMisuseListener();

            if (languageManager == null)
                languageManager = new LanguageManager<>();

            if (dispatcherManager == null)
                dispatcherManager = new DispatcherManager();

            if (injector == null)
                injector = Guice.createInjector();

            if (validationManager == null)
                validationManager = new ValidationManager(injector, context);

            if (testManager == null)
                testManager = new TestManager(context, injector);

            if (adapterManager == null)
                adapterManager = new AdapterManager(injector, context.getAdapters());

            if (responseManager == null)
                responseManager = new ResponseManager(injector, context.getProviders());

            return new Commandler(
                context,
                misuseManager,
                languageManager,
                responseManager,
                dispatcherManager,
                validationManager,
                adapterManager,
                testManager,
                injector
            );
        }

        public Context getContext() {
            return context;
        }

        public MisuseHandler getMisuseManager() {
            return misuseManager;
        }

        public Builder setMisuseManager(MisuseHandler misuseManager) {
            this.misuseManager = misuseManager;
            return this;
        }

        public LanguageInterface getLanguageManager() {
            return languageManager;
        }

        public Builder setLanguageManager(LanguageInterface languageManager) {
            this.languageManager = languageManager;
            return this;
        }

        public ResponseManager getResponseManager() {
            return responseManager;
        }

        public Builder setResponseManager(ResponseManager responseManager) {
            this.responseManager = responseManager;
            return this;
        }

        public DispatcherManager getDispatcherManager() {
            return dispatcherManager;
        }

        public Builder setDispatcherManager(DispatcherManager dispatcherManager) {
            this.dispatcherManager = dispatcherManager;
            return this;
        }

        public ValidationManager getValidationManager() {
            return validationManager;
        }

        public Builder setValidationManager(ValidationManager validationManager) {
            this.validationManager = validationManager;
            return this;
        }

        public AdapterManager getAdapterManager() {
            return adapterManager;
        }

        public Builder setAdapterManager(AdapterManager adapterManager) {
            this.adapterManager = adapterManager;
            return this;
        }

        public TestManager getTestManager() {
            return testManager;
        }

        public Builder setTestManager(TestManager testManager) {
            this.testManager = testManager;
            return this;
        }

        public Injector getInjector() {
            return injector;
        }

        public Builder setInjector(Injector injector) {
            this.injector = injector;
            return this;
        }
    }
}

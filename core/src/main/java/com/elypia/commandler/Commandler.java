package com.elypia.commandler;

import com.elypia.commandler.interfaces.*;
import com.elypia.commandler.managers.*;
import com.elypia.commandler.misuse.CommandMisuseListener;

import java.util.Objects;

/**
 * Commandler brings all components together to process
 * command events to perform method calls and return
 * the result.
 */
public class Commandler {

    /** The collation of MetaData used to initialize this Commandler instance. */
    private Context context;

    /** Dependency injection injector to inject dependendies into provided implementations. */
    private InjectionManager injectionManager;

    /** Handler for errors, both end-user errors and exceptions. */
    private MisuseHandler misuseManager;

    /** Obtain externally managed strings for internationalization. */
    private LanguageInterface languageManager;

    /** Take the result of a method and make it something sendable to the service. */
    private ResponseManager responseManager;

    /** Dispatch various types of commands this instance is aware of. */
    private DispatcherManager dispatcherManager;

    /** Validate the parameters given are appropriate for the command executed. */
    private ValidationManager validationManager;

    /** Adapt parameters as Java objects for methods to use. */
    private AdapterManager adapterManager;

    /** The test runner to verify the integrity of modules through runtime. */
    private TestManager testManager;

    public Commandler(
        Context context,
        InjectionManager injectionManager,
        MisuseHandler misuseManager,
        LanguageInterface languageManager,
        ResponseManager responseManager,
        DispatcherManager dispatcherManager,
        ValidationManager validationManager,
        AdapterManager adapterManager,
        TestManager testManager
    ) {
        this.context = Objects.requireNonNull(context);
        this.injectionManager = Objects.requireNonNull(injectionManager);
        this.misuseManager = Objects.requireNonNull(misuseManager);
        this.languageManager = Objects.requireNonNull(languageManager);
        this.responseManager = Objects.requireNonNull(responseManager);
        this.dispatcherManager = Objects.requireNonNull(dispatcherManager);
        this.validationManager = Objects.requireNonNull(validationManager);
        this.adapterManager = Objects.requireNonNull(adapterManager);
        this.testManager = Objects.requireNonNull(testManager);

        injectionManager.addModules(new CommandlerModule(this));
    }

    public Context getContext() {
        return context;
    }

    public InjectionManager getInjector() {
        return injectionManager;
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

    public static class Builder {

        private Context context;
        private InjectionManager injectionManager;
        private MisuseHandler misuseManager;
        private LanguageInterface languageManager;
        private ResponseManager responseManager;
        private DispatcherManager dispatcherManager;
        private ValidationManager validationManager;
        private AdapterManager adapterManager;
        private TestManager testManager;

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

            if (injectionManager == null)
                injectionManager = new InjectionManager();

            if (validationManager == null)
                validationManager = new ValidationManager(injectionManager, context);

            if (testManager == null)
                testManager = new TestManager(injectionManager, context);

            if (adapterManager == null)
                adapterManager = new AdapterManager(injectionManager, context.getAdapters());

            if (responseManager == null)
                responseManager = new ResponseManager(injectionManager, context.getProviders());

            return new Commandler(
                context,
                injectionManager,
                misuseManager,
                languageManager,
                responseManager,
                dispatcherManager,
                validationManager,
                adapterManager,
                testManager
            );
        }

        public Context getContext() {
            return context;
        }

        public InjectionManager getInjector() {
            return injectionManager;
        }

        public Builder setInjector(InjectionManager injectionManager) {
            this.injectionManager = injectionManager;
            return this;
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
    }
}

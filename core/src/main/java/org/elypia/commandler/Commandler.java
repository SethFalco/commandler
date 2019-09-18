package org.elypia.commandler;

import com.google.inject.*;
import org.apache.commons.configuration2.Configuration;
import org.elypia.commandler.api.*;
import org.elypia.commandler.configuration.CommandlerConfiguration;

import javax.inject.Singleton;
import java.util.List;

/**
 * The root {@link Commandler} class, this ultimately enables your
 * {@link Commandler} Application with your configuration and runtime dependencies.
 *
 * There are two main means of configurable {@link Commandler}.
 *
 * <strong>Static Configuration:</strong> This entails configuration files either in the
 * classpath or externally, and {@link java.lang.annotation.Annotation}s.
 * <strong>Dependency Injection Modules:</strong> This entails overriding runtime dependencies
 * for {@link Guice} to inject into dependencies and {@link Controller}s.
 */
@Singleton
public class Commandler {

    /** The configuration for this instance of {@link Commandler}. */
    private CommandlerConfiguration config;

    /** The {@link Guice} {@link Injector} to inject dependencies for Commandler. */
    private Injector injector;

    /** A list of action listeners to send action events to. */
    private List<ActionListener> listeners;

    /**
     * Instantiate the default instance of Commandler.
     * This will load the {@link Configuration} from the default sources and add
     * the defalt {@link ActionListener} implementation.
     *
     * @see ActionHandler The default {@link ActionListener} implementation.
     */
    public Commandler() {
        this(Guice.createInjector(Stage.PRODUCTION));
    }

    /**
     * Instantiate {@link Commandler} using a {@link CommandlerConfiguration} managed
     * externally and passwed over.
     *
     * @param injector Dependency injector to configure and setup Commandler.
     */
    public Commandler(Injector injector) {
        this(injector, injector.getInstance(CommandlerConfiguration.class));
    }

    public Commandler(Injector injector, CommandlerConfiguration config) {
        this.injector = injector;
        this.config = config;
    }

    public void addListeners(ActionListener... listeners) {
        this.listeners.addAll(List.of(listeners));
    }

    public CommandlerConfiguration getConfig() {
        return config;
    }
}

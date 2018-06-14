package com.elypia.commandler;

import com.elypia.commandler.confiler.*;
import com.elypia.commandler.modules.*;
import com.elypia.commandler.parsing.IParamParser;
import com.elypia.commandler.sending.IMessageSender;
import com.elypia.commandler.validation.*;
import net.dv8tion.jda.core.JDA;

import java.lang.annotation.Annotation;
import java.util.*;

/**
 * This is the manager class which acts as the link between
 * Commandler and JDA to distribute messages and query information
 * to deliver to commands / methods.
 */

public class Commandler {

    /**
     * The JDA instance to retrieve and process messages from.
     * Commandler will create a {@link Dispatcher} and register
     * it to this JDA instance in order to process commands to modules
     * registered via {@link #registerModule(CommandHandler)}.
     */

    private final JDA jda;

    /**
     * This is the <strong>default</strong> prefix for your bot.
     * This will assume the value of <em>!</em> if not specified.
     */

    private final Confiler confiler;

    /**
     * All registered modules / command handlers with this JDACommandler.
     */

    private Collection<CommandHandler> handlers;

    private Dispatcher dispatcher;

    /**
     * Any root alias, this could be a module alias or an alias to a
     * static commmand. We need to keep track of a list to ensure a root
     * alias isn't registered more than once.
     */

    private Collection<String> rootAlises;

    /**
     * See {@link #Commandler(JDA, Confiler)}
     * Creates a JDACommandler instance with the default prefix as "!".
     *
     * @param jda The JDA instance to register the {@link Dispatcher}.
     */

    public Commandler(final JDA jda) {
        this(jda, "!");
    }

    public Commandler(final JDA jda, String prefix) {
        this(jda, new DefaultConfiler(prefix));
    }

    /**
     * Creates an instance of the JDACommandler with the provided JDA and prefix.
     * This prefix is used as a default prefix if no method to obtain the prefix
     * per event is registered.
     *
     * @param jda The JDA instance to register the {@link Dispatcher}.
     * @param confiler The <strong>default</strong> prefix for command handling.
     */

    public Commandler(final JDA jda, Confiler confiler) {
        this.jda = jda;
        this.confiler = confiler;
        handlers = new ArrayList<>();
        rootAlises = new ArrayList<>();

        dispatcher = new Dispatcher(this);

        if (jda != null)
            jda.addEventListener(dispatcher);

        registerModule(new HelpModule());
    }

    public Dispatcher getDispatcher() {
        return dispatcher;
    }

    /**
     * Register multiple handlers at once.
     * Calls {@link #registerModule(CommandHandler)} for each module specified.
     *
     * @param handlers A list of modules to register at once.
     */

    public void registerModules(CommandHandler... handlers) {
        for (CommandHandler handler : handlers)
            registerModule(handler);
    }

    /**
     * Register a module with JDACommandler in order
     * to execute commands in the module.
     *
     * @param handler The command handler / module to register.
     */

    public void registerModule(CommandHandler handler) {
        if (!handler.init(jda, this))
            handler.setEnabled(false);

        handlers.add(handler);
    }

    public <T> void registerParser(Class<T> clazz, IParamParser<T> parser) {
        dispatcher.getParser().registerParser(clazz, parser);
    }

    public <T> void registerSender(Class<T> clazz, IMessageSender<T> sender) {
        dispatcher.getSender().registerSender(clazz, sender);
    }

    public <T extends Annotation> void registerValidator(Class<T> clazz, IParamValidator<?, T> validator) {
        dispatcher.getValidator().registerValidator(clazz, validator);
    }

    public <T extends Annotation> void registerValidator(Class<T> clazz, ICommandValidator<T> validator) {
        dispatcher.getValidator().registerValidator(clazz, validator);
    }

    public JDA getJDA() {
        return jda;
    }

    /**
     * @return The default / global prefix.
     */

    public Confiler getConfiler() {
        return confiler;
    }

    public Collection<CommandHandler> getHandlers() {
        return handlers;
    }

    public Collection<String> getRootAlises() {
        return rootAlises;
    }
}

package com.elypia.commandler;

import com.elypia.commandler.building.Builder;
import com.elypia.commandler.impl.*;
import com.elypia.commandler.metadata.MetaCommand;
import com.elypia.commandler.modules.*;
import com.elypia.commandler.impl.IParamParser;
import com.elypia.commandler.building.IMessageBuilder;
import com.elypia.commandler.parsing.Parser;
import com.elypia.commandler.validation.Validator;
import net.dv8tion.jda.core.JDA;

import java.lang.annotation.Annotation;
import java.util.*;



/**
 * This is the manager class and core of the {@link Commandler}
 * framework. This centralises all usage of Commandler for
 * implementation.
 *
 * @param <C> The client this {@link Commandler} manages.
 * @param <E> The type of event we're handling.
 * @param <M> The type of message we're expecting to send and receieve.
 */

public abstract class Commandler<C, E, M> {

    /**
     * The client represents the platform you're chatting on
     * and where {@link E messages} are being receieved.
     */

    protected C client;

    /**
     * This is the configuration for {@link Commandler} and
     * must implement methods that will be used internally.
     *
     * @see Confiler for a default implementation
     * of the Confiler.
     */

    protected IConfiler<E, M> confiler;

    /**
     * The event handler registered to JDA to recieved events for Commandler
     * to handle.
     */

    protected IDispatcher<E, M> dispatcher;

    /**
     * All registered modules / commands handlers with this JDACommandler.
     */

    protected List<CommandHandler> handlers;

    /**
     * Any root alias, this could be a module alias or an alias to a
     * static commmand. We need to keep track of a list to ensure a root
     * alias isn't registered more than once.
     */

    protected Collection<String> rootAlises;

    /**
     * A global map of all commands with reference to an ID.
     * This is used for reaction handling as they are stored by ID so
     * it is faster to obtain the command by ID rather than search for it
     * though iterating {@link #handlers}.
     */

    protected Map<Integer, MetaCommand> commands;



    public Commandler(String... prefixes) {
        this(new Confiler<>(prefixes));
    }

    /**
     * Creates an instance of the Commandler with the provided Confiler.
     * This could be the {@link DefaultConfiler} or a class made using the
     * {@link Confiler} interface. The Confiler is essentially some basic settings
     * and functions for the bot to use, such as how to get the prefix if for example
     * you want a custom prefix per guild.
     *
     * @param confiler The configuration for Commandler.
     */

    public Commandler(Confiler<E> confiler) {
        this.confiler = confiler;
        dispatcher = new Dispatcher<E, M>(this);
        handlers = new ArrayList<>();
        rootAlises = new ArrayList<>();
        commands = new HashMap<>();

        registerModule(new HelpModule());
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
     * Register a module with Commandler in order
     * to execute commands in the module.
     *
     * @param handler The commands handler / module to register.
     */

    public void registerModule(CommandHandler handler) {
        boolean enabled = handler.init(jda, this);

        handler.setEnabled(enabled);
        handlers.add(handler);

        Collections.sort(handlers);
    }

    public M trigger(E event, String input) {
        return dispatcher.processEvent(event, input);
    }



    public C getClient() {
        return jda;
    }

    /**
     * Set the JDA instance this initialised Commandler instance should
     * register too.
     *
     * @param jda The client object for your bot.
     */

    public void setJDA(C client) {
        this.jda = Objects.requireNonNull(jda);
        jda.addEventListener(dispatcher);
    }

    public IDispatcher getDispatcher() {
        return dispatcher;
    }

    public void setDispatcher(IDispatcher<E, M> dispatcher) {
        this.dispatcher = dispatcher;
    }

    public IConfiler<E> getConfiler() {
        return confiler;
    }

    public Collection<CommandHandler> getHandlers() {
        return handlers;
    }

    public Collection<String> getRootAlises() {
        return rootAlises;
    }

    public Map<Integer, MetaCommand> getCommands() {
        return commands;
    }
}

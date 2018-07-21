package com.elypia.commandler;

import com.elypia.commandler.annotations.*;
import com.elypia.commandler.components.*;
import com.elypia.commandler.impl.*;
import com.elypia.commandler.metadata.*;
import com.elypia.commandler.components.Validators;

import java.lang.annotation.Annotation;
import java.util.*;

/**
 * This is the manager class and core of the {@link Commandler}
 * framework. This centralises all usage of Commandler for
 * implementation. This and the relevent classes must be implemented
 * for the client you wish to integrate with before a bot can be made.
 *
 * @param <C> The client this {@link Commandler} manages.
 * @param <E> The type of event we're handling.
 * @param <M> The type of message we're expecting to send and receieve.
 */

public abstract class Commandler<C, E, M> {

    /**
     * The client represents the platform you're chatting on
     * and where {@link E messages} are being receieved. <br>
     * If a client is not applicable to this implementation, this can be
     * set to {@link Void} to indicate this.
     */

    protected C client;

    /**
     * This is the configuration for {@link Commandler} and
     * must implement methods that will be used internally.
     *
     * @see Confiler for a default implementation
     * of the Confiler.
     */

    protected IConfiler<C, E, M> confiler;

    /**
     * The proxy between the {@link #client} event handler, and {@link Commandler}.
     * This will have an implementation to process the command into something
     * {@link Commandler} can interpret.
     */

    protected IDispatcher<C, E, M> dispatcher;

    /**
     * All registered modules with this {@link Commandler}.
     */

    protected List<IHandler<C, E, M>> handlers;

    /**
     * Any root alias, this could be a module alias or an alias to a
     * static commmand. We need to keep track of a list to ensure a root
     * alias isn't registered more than once, we also use this as a global
     * reference to obtain the a module.
     */

    protected Map<String, MetaModule<C, E, M>> roots;

    /**
     * Any commands with a non-zero {@link Command#id()}. This is a
     * pool of commands for referencing whenever we need an
     * {@link Overload} so we know what the parent {@link Command} is.
     * This can also be reused for implementation to dictate more actions.
     */

    protected Map<Integer, MetaCommand<C, E, M>> commands;

    protected Parsers parser;

    protected Validators validator;

    protected Builders<M> builder;

    /**
     * Creates an instance of the {@link Commandler} framework.
     * This requires an {@link IConfiler} to specify configuration for this
     * platform, and an {@link IDispatcher} to specify how to process the
     * {@link E event} and / or {@link M message} received from the {@link C client}.
     */

    public Commandler(IConfiler<C, E, M> confiler) {
        this.confiler = confiler;

        handlers = new ArrayList<>();
        roots = new HashMap<>();
        commands = new HashMap<>();
        parser = new Parsers(this);
        validator = new Validators();
        builder = new Builders<>();
    }

    public void setDispatcher(IDispatcher<C, E, M> dispatcher) {
        this.dispatcher = dispatcher;
    }

    /**
     * Register a module with Commandler in order
     * to execute commands in the module.
     *
     * @param handler The commands handler / module to register.
     */

    public void registerModule(IHandler<C, E, M> handler) {
        boolean enabled = handler.init(this);

        handler.setEnabled(enabled);
        handlers.add(handler);

//        Collections.sort(handlers);
    }

    public M trigger(E event, String input) {
        return dispatcher.processEvent(event, input);
    }


    /**
     * You can register custom parsers, this allows Commandler to know
     * how to parse certain objects as method parameters for you.
     *
     * @param clazz The type of class this parser will parse.
     * @param parser The parser which implements {@link IParser}, this has the method
     * which will interpret the String as our class object, else invalidate the command.
     * @param <T> The type of class this parses.
     */

    public void registerParser(IParser parser, Class...types) {
        this.parser.registerParser(parser, types);
    }

    public void registerBuilder(IBuilder<?, M> builder, Class...types) {
        this.builder.registerBuilder(builder, types);
    }

    public <T extends Annotation> void registerValidator(Class<T> clazz, IParamValidator<?, T> validator) {
        this.validator.registerValidator(clazz, validator);
    }

    public <T extends Annotation> void registerValidator(Class<T> clazz, ICommandValidator<T> validator) {
        this.validator.registerValidator(clazz, validator);
    }

    public C getClient() {
        return client;
    }

    public void setClient(C client) {
        this.client = Objects.requireNonNull(client);
    }

    public IConfiler<C, E, M> getConfiler() {
        return confiler;
    }

    public IDispatcher getDispatcher() {
        return dispatcher;
    }

    public Collection<IHandler<C, E, M>> getHandlers() {
        return handlers;
    }

    public Map<String, MetaModule<C, E, M>> getRoots() {
        return roots;
    }

    public Map<Integer, MetaCommand<C, E, M>> getCommands() {
        return commands;
    }

    public Parsers getParser() {
        return parser;
    }

    public Builders<M> getBuilder() {
        return builder;
    }

    public Validators getValidator() {
        return validator;
    }

}

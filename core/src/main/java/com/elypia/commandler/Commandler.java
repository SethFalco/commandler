package com.elypia.commandler;

import com.elypia.commandler.impl.*;
import com.elypia.commandler.interfaces.*;
import org.slf4j.*;

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
public class Commandler<C, E, M> {

    private static final Logger logger = LoggerFactory.getLogger(Commandler.class);

    private ModulesContext context;

    /**
     * The proxy between the {@link #client} event handler, and {@link Commandler}.
     * This will have an implementation to process the command into something
     * {@link Commandler} can interpret.
     */
    protected ICommandProcessor<C, E, M> processor;

    private IMisuseHandler<C, E, M> listener;

    private LanguageEngine<E> engine;

    /**
     * The client represents the platform you're chatting on
     * and where {@link E messages} are being receieved. <br>
     * If a client is not applicable to this implementation, this can be
     * set to {@link Void} to indicate this.
     */
    protected C client;

    /**
     * All registered modules with this {@link Commandler}.
     */
    protected List<Handler<C, E, M>> handlers;

    protected ParseRegister parser;

    protected BuildRegister<M> builder;

    protected CommandValidator commandValidator;

    private String prefix;

    private String website;

    public Commandler(CommandlerBuilder<C, E, M> builder) {
        this.context = null;
        this.processor = null;
        this.listener = null;
        this.engine = null;
        this.client = null;


        processor = new CommandProcessor<>(this);
    }

    public M trigger(E event, String input) {
        return trigger(event, input, true);
    }

    public M trigger(E event, String input, boolean send) {
        return processor.dispatch(event, input, send);
    }

    /**
     * You can register custom parsers, this allows Commandler to know
     * how to parse certain objects as method parameters for you.
     *
     * @param parser The parser which implements {@link IParser}, this has the method
     * which will interpret the String as our class object, else invalidate the command.
     * @param types The data-types this parser can work with.
     */
    public void registerParser(IParser parser, Class...types) {
        this.parser.registerParser(parser, types);
    }

    public void registerBuilder(IBuilder<?, ?, M> builder, Class...types) {
        this.builder.registerBuilder(builder, types);
    }

    public ModulesContext getContext() {
        return context;
    }

    public C getClient() {
        return client;
    }

    public void setClient(C client) {
        this.client = Objects.requireNonNull(client);
    }

    public ICommandProcessor getProcessor() {
        return processor;
    }

    public Collection<Handler<C, E, M>> getHandlers() {
        return handlers;
    }

    public ParseRegister getParser() {
        return parser;
    }

    public BuildRegister<M> getBuilder() {
        return builder;
    }

    public CommandValidator getCommandValidator() {
        return commandValidator;
    }

    public IMisuseHandler<C, E, M> getMisuseListener() {
        return listener;
    }

    public LanguageEngine<E> getEngine() {
        return engine;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getWebsite() {
        return website;
    }
}

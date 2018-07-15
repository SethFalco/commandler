package com.elypia.commandler;

import com.elypia.commandler.confiler.*;
import com.elypia.commandler.impl.*;
import com.elypia.commandler.metadata.MetaCommand;
import com.elypia.commandler.modules.*;
import com.elypia.commandler.impl.IParamParser;
import com.elypia.commandler.building.IMessageBuilder;
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

    private JDA jda;

    /**
     * This is the <strong>default</strong> prefix for your bot.
     * This will assume the value of <em>!</em> if not specified.
     */

    private final IConfiler confiler;

    /**
     * The event handler registered to JDA to recieved events for Commandler
     * to handle.
     */

    private Dispatcher dispatcher;

    /**
     * All registered modules / commands handlers with this JDACommandler.
     */

    private List<CommandHandler> handlers;

    /**
     * Any root alias, this could be a module alias or an alias to a
     * static commmand. We need to keep track of a list to ensure a root
     * alias isn't registered more than once.
     */

    private Collection<String> rootAlises;

    /**
     * A global map of all commands with reference to an ID.
     * This is used for reaction handling as they are stored by ID so
     * it is faster to obtain the command by ID rather than search for it
     * though iterating {@link #handlers}.
     */

    private Map<Integer, MetaCommand> commands;


    public Commandler(String prefix) {
        this(prefix, null);
    }

    public Commandler(String prefix, String helpUrl) {
        this(new DefaultConfiler(prefix, helpUrl));
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

    public Commandler(Confiler confiler) {
        this.confiler = confiler;
        dispatcher = new Dispatcher(this);
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

    /**
     * You can register custom parsers, this allows Commandler to know
     * how to parse certain objects as method parameters for you.
     *
     * @param clazz The type of class this parser will parse.
     * @param parser The parser which implements {@link IParamParser}, this has the method
     * which will interpret the String as our class object, else invalidate the command.
     * @param <T> The type of class this parses.
     */

    public <T> void registerParser(Class<T> clazz, IParamParser<T> parser) {
        dispatcher.getParser().registerParser(clazz, parser);
    }

    public <T> void registerSender(Class<T> clazz, IMessageBuilder<T> sender) {
        dispatcher.getBuilder().registerBuilder(clazz, sender);
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
     * Set the JDA instance this initialised Commandler instance should
     * register too.
     *
     * @param jda The client object for your bot.
     */

    public void setJDA(JDA jda) {
        this.jda = Objects.requireNonNull(jda);
        jda.addEventListener(dispatcher);
    }

    public Dispatcher getDispatcher() {
        return dispatcher;
    }

    public IConfiler getConfiler() {
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

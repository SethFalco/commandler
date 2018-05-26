package com.elypia.commandler.jda;

import com.elypia.commandler.CommandHandler;
import com.elypia.commandler.annotations.Module;
import net.dv8tion.jda.core.JDA;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * This is the manager class which acts as the link between
 * Commandler and JDA to distribute messages and query information
 * to deliver to commands / methods.
 */

public class JDACommandler {

    /**
     * The JDA instance to retrieve and process messages from.
     * JDACommandler will create a {@link JDADispatcher} and register
     * it to this JDA instance in order to process commands to modules
     * registered via {@link #registerModule(JDACommandHandler)}.
     */

    private final JDA jda;

    /**
     * This is the <strong>default</strong> prefix for your bot.
     * This will assume the value of <em>!</em> if not specified.
     */

    private final String prefix;

    /**
     * All registered modules / command handlers with this JDACommandler.
     */

    private Collection<CommandHandler> handlers;

    /**
     * See {@link #JDACommandler(JDA, String)}
     * Creates a JDACommandler instance with the default prefix as "!".
     *
     * @param jda The JDA instance to register the {@link JDADispatcher}.
     */

    public JDACommandler(final JDA jda) {
        this(jda, "!");
    }

    /**
     * Creates an instance of the JDACommandler with the provided JDA and prefix.
     * This prefix is used as a default prefix if no method to obtain the prefix
     * per event is registered.
     *
     * @param jda The JDA instance to register the {@link JDADispatcher}.
     * @param prefix The <strong>default</strong> prefix for command handling.
     */

    public JDACommandler(final JDA jda, final String prefix) {
        this.jda = jda;
        this.prefix = prefix;
        handlers = new ArrayList<>();

        jda.addEventListener(new JDADispatcher(this));
    }

    /**
     * Register multiple handlers at once.
     * Calls {@link #registerModule(JDACommandHandler)} for each module specified.
     *
     * @param handlers A list of modules to register at once.
     */

    public void registerModules(JDACommandHandler... handlers) {
        for (JDACommandHandler handler : handlers)
            registerModule(handler);
    }

    /**
     * Register a module with JDACommandler in order
     * to execute commands in the module.
     *
     * @param handler The command handler / module to register.
     */

    public void registerModule(JDACommandHandler handler) {
        Module module = handler.getClass().getAnnotation(Module.class);

        if (module == null)
            throw new IllegalArgumentException("CommandHandler doesn't contain Module annotation!");

        Collection<String> aliases = new ArrayList<>();

        for (String alias : module.aliases())
            aliases.add(alias.toLowerCase());

        for (CommandHandler h : handlers) {
            Module m = h.getClass().getAnnotation(Module.class);

            Collection<String> existing = Arrays.asList(m.aliases());

            if (!Collections.disjoint(aliases, existing))
                throw new IllegalArgumentException("CommandHandler contains alias which is already registered.");
        }

        handler.setJDA(jda);
        handlers.add(handler);
    }

    public JDA getJDA() {
        return jda;
    }

    /**
     * @return The default / global prefix.
     */

    public String getPrefix() {
        return prefix;
    }

    public Collection<CommandHandler> getHandlers() {
        return handlers;
    }
}

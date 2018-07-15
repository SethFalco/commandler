package com.elypia.commandler.impl;

import com.elypia.commandler.*;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.DefaultConfiler;
import com.elypia.commandler.annotations.validation.command.Developer;
import com.elypia.commandler.events.CommandEvent;
import com.elypia.commandler.pages.PageBuilder;

/**
 * Confiler is the config object for Commandler and allows the developer
 * to customise how it parses commands and parameters, or how to obtain the prefix. <br>
 * <br>
 * @see Confiler for the default implementation, it is recommend to extend and {@link Override}
 * the methods from there.
 */

public interface IConfiler<E, M> {

    /**
     * Parse the event {@link E} spawned by the client using
     * the {@link String content} as input. This input may not
     * actually belong to the command in cases where a
     * {@link CommandEvent#trigger(String)} is performed.
     *
     * @param event The event spanwed by the client.
     * @param content The content of the meessage.
     * @return The parsed contents of the message, or null if the command is invalid.
     */

    CommandInput processEvent(Commandler commandler, E event, String content);

    IMisuseListener<M> getMisuseListener();

    /**
     * The prefix for this event. This can be set to return a static
     * {@link String}. This exists for bots that have a customizable prefix
     * in order to check where the commands was performed and obtain the correct
     * prefix to use in {@link #processEvent(E, String)} or when
     * queried elsewhere in the code.
     *
     * @param event The message event as provided by the client.
     * @return The prefixes to be used for this particular event.
     */

    String[] getPrefixes(Commandler commandler, E event);

    /**
     * This is used for generating help commands. As all modules and commands
     * dictate help though the provided annotations there needs to be a way
     * for users to be able to configure how help is obtained if logic is required.
     * This allows for the help to be a key for example which can query a script
     * file or obtain the language from the event to select the correct
     * script to send. <br>
     * <br>
     * It may be worth looking into <a href="https://gitlab.com/Elypia/ElyScript">ElyScript</a>
     * to help make managing scripts and internationalization easier.
     *
     * @param event The message event as provided by the client.
     * @param key The help as specified in the annotations, for
     *            example in: {@link Module#description()} or {@link Command#help()}.
     * @return The help {@link String} which should be displayed to users.
     * @see <a href="https://gitlab.com/Elypia/ElyScript">ElyScript</a>
     */

    String getHelp(Commandler commandler, E event, String key);

    /**
     * Get the website which the help is hosted at if available.
     *
     * @param event The message event as provide the by client.
     * @return The url the help is hosted at.
     * @see PageBuilder for dynamic webpage generation.
     */

    String getHelpUrl(Commandler commandler, E event);

    /**
     * The developers of this application, this can be used internally
     * by validators to ensure the developers are always able to perform
     * commands but also limit other users, for example with {@link Developer}.
     *
     * @return An ordered list of identifiers to identify each developer.
     * @see Developer To limit a {@link Command} or {@link Module}
     *      to only {@link #getDevelopers(Commandler, E) developers} can perform them.
     */

    String[] getDevelopers(Commandler commandler, E event);
}

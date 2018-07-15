package com.elypia.commandler.impl;

import com.elypia.commandler.CommandInput;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.confiler.DefaultConfiler;
import com.elypia.commandler.events.CommandEvent;

/**
 * Confiler is the config object for Commandler and allows the developer
 * to customise how it parses commands and parameters, or how to obtain the prefix. <br>
 * <br>
 * @see DefaultConfiler for the default implementation, it is recommend to extend and {@link Override}
 * the methods from there.
 */

public interface IConfiler<T> {

    /**
     * Parse the event {@link T} spawned by the client using
     * the {@link String content} as input. This input may not
     * actually belong to the command in cases where a
     * {@link CommandEvent#trigger(String)} is performed.
     *
     * @param event The event spanwed by the client.
     * @param content The content of the meessage.
     * @return The parsed contents of the message, or null if the command is invalid.
     */

    CommandInput processEvent(T event, String content);

    /**
     * The prefix for this event. This can be set to return a static
     * {@link String}. This exists for bots that have a customizable prefix
     * in order to check where the commands was performed and obtain the correct
     * prefix to use in {@link #processEvent(T, String)} or when
     * queried elsewhere in the code.
     *
     * @param event The message event as provided by the client.
     * @return The prefix to be used for this particular event.
     */

    String getPrefix(T event);

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

    String getHelp(T event, String key);

    IReactionListener getReactionController();


    long[] getDevelopers();
}

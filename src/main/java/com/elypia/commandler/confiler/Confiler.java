package com.elypia.commandler.confiler;

import net.dv8tion.jda.core.events.message.*;

import java.util.regex.Pattern;

/**
 * Confiler is the config object for Commandler and allows the developer
 * to customise how it parses commands and parameters, or how to obtain the prefix. <br>
 * <br>
 * @see DefaultConfiler for the default implementation.
 */

public interface Confiler {

    /**
     * Commandler uses regular expression to validate and split up the message
     * into it's various components. A command <strong>should</strong> have the following
     * capture groups: <br>
     * <br>
     * <strong>prefix</strong>: the prefix to match <br>
     * <strong>module</strong>: the module or handler the command is in <br>
     * <strong>submodule</strong>: the submodule or child module <br>
     * <strong>command</strong>: the command the user wishes to perform <br>
     * <strong>params</strong>: all parameters for the command, these are collected together
     *
     * @param event The message event as provided by JDA.
     * @return The regex used to match against the message.
     */

    Pattern getCommandRegex(GenericMessageEvent event);

    /**
     * Commandler using regular expression to split the parameters up. <br>
     * <em>It's discouraged to change this.</em>
     *
     * @param event The message event as provided by JDA.
     * @return The regex used to split up all parameters.
     */

    Pattern getParamRegex(GenericMessageEvent event);

    /**
     * The prefix for this event. This can be set to return a static
     * String. This exists for bots that have a customizable prefix per guild
     * in order to check where the command was performed and obtain the correct
     * prefix to insert into {@link #getCommandRegex(GenericMessageEvent)}.
     *
     * @param event The message event as provided by JDA.
     * @return The prefix to be used for this particular event.
     */

    String getPrefix(GenericMessageEvent event);
}

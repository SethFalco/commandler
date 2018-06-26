package com.elypia.commandler.confiler;

import com.elypia.commandler.confiler.reactions.IReactionController;
import com.elypia.commandler.events.CommandEvent;
import net.dv8tion.jda.core.events.message.GenericMessageEvent;

import java.util.regex.Pattern;

/**
 * Confiler is the config object for Commandler and allows the developer
 * to customise how it parses commands and parameters, or how to obtain the prefix. <br>
 * <br>
 * @see DefaultConfiler for the default implementation, it is recommend to extend and {@link Override}
 * the methods from there.
 */

public interface Confiler {

    /**
     * Commandler uses regular expression to validate and split up the message
     * into it's various components. A commands <strong>should</strong> have the following
     * capture groups: <br>
     * <br>
     * <strong>prefix</strong>: the prefix to match <br>
     * <strong>alias</strong>: the root alias, this may refer to a module, or static commands <br>
     * <strong>commands</strong>: the commands the user wishes to perform <br>
     * <strong>params</strong>: all parameters for the commands, these are collected together
     *
     * @param event The message event as provided by JDA.
     * @return The regex used to match against the message.
     */

    Pattern getCommandRegex(GenericMessageEvent event);

    /**
     * Commandler using regular expression to split the parameters up. <br>
     *
     * @param event The message event as provided by JDA.
     * @return The regex used to split up all parameters.
     */

    Pattern getParamRegex(GenericMessageEvent event);

    Pattern getSplitRegex(GenericMessageEvent event);

    /**
     * The prefix for this event. This can be set to return a static
     * String. This exists for bots that have a customizable prefix per guild
     * in order to check where the commands was performed and obtain the correct
     * prefix to insert into {@link #getCommandRegex(GenericMessageEvent)}.
     *
     * @param event The message event as provided by JDA.
     * @return The prefix to be used for this particular event.
     */

    String getPrefix(GenericMessageEvent event);

    long[] getDevelopers();

    String getHelpUrl(CommandEvent event);

    IReactionController getReactionController();
}

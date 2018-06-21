package com.elypia.commandler.confiler;

import net.dv8tion.jda.core.events.message.GenericMessageEvent;

import java.util.regex.Pattern;

/**
 * If you're configuing Commandler, it's a good idea to extend and {@link Override}
 * the {@link #getPrefix(GenericMessageEvent)} method here to get you going.
 */

public class DefaultConfiler implements Confiler {

    /**
     * The default commands regex, this matches the commands to see if its
     * valid and returns the matches as groups.
     */

    private static final String COMMAND_REGEX = "(?i)\\A(?:\\\\?<@!?%s>\\s{0,2}|\\Q%s\\E)(?<module>[A-Z\\d]+(?![^A-Z\\d\\s]+))(?:\\s+(?<commands>[A-Z\\d]+(?![^A-Z\\d\\s])))?(?:\\s+(?<params>(?:.|\\s)+))?\\Z";

    /**
     * The default params regex, this matches every argument in the commands,
     * any comma seperated <strong>args</strong> will be split by comma as a list.
     */

    private static final Pattern PARAM_PATTERN = Pattern.compile("(?:(?:\"(?:\\\\\"|[^\"])*\"|[^\\s,]+)(?:\\s*,\\s*)?)+");

    private static final Pattern SPLIT_PATTERN = Pattern.compile("\"(?<quote>(?:\\\\\"|[^\"])*)\"|(?<word>[^\\s,]+)");

    private final String DEFAULT_PREFIX;

    /**
     * The first time the commands regular expression is queried, we take
     * {@link #COMMAND_REGEX} and insert the bots id and store it here to save
     * us from populating it per commands.
     */

    private String commandRegex;

    /**
     * Calls {@link #DefaultConfiler(String)} and defaults the bot prefix to <strong>!</strong>
     *
     * @see DefaultConfiler(String)
     */

    public DefaultConfiler() {
        this("!");
    }

    /**
     * Instantiate the Default configuration for Commandler with a different prefix.
     *
     * @param prefix The default prefix for the bot.
     */

    public DefaultConfiler(String prefix) {
        DEFAULT_PREFIX = prefix;
    }

    /**
     * Builds are returns the regular expression {@link Pattern} used
     * for matching against commands. This allows the user to insert data such as
     * the body ID or {@link #getPrefix(GenericMessageEvent) prefix} per commands if
     * it may vary depending on the event.
     *
     * @param event The message event as provided by JDA.
     * @return The pattern to match again user input.
     */

    @Override
    public Pattern getCommandRegex(GenericMessageEvent event) {
        if (commandRegex == null) {
            String id = event.getJDA().getSelfUser().getId();
            commandRegex = String.format(COMMAND_REGEX, id, "%s");
        }

        return Pattern.compile(String.format(commandRegex, getPrefix(event)));
    }

    /**
     * Commandler using regular expression to split the parameters up. <br>
     * <em>It's discouraged to change this.</em>
     *
     * @param event The message event as provided by JDA.
     * @return The regex used to split up all parameters.
     */

    @Override
    public Pattern getParamRegex(GenericMessageEvent event) {
        return PARAM_PATTERN;
    }

    public Pattern getSplitRegex(GenericMessageEvent event) {
        return SPLIT_PATTERN;
    }

    /**
     * @param event The message event as provided by JDA.
     * @return The prefix to be used for this particular event.
     */

    @Override
    public String getPrefix(GenericMessageEvent event) {
        return DEFAULT_PREFIX;
    }
}

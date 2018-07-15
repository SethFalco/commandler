package com.elypia.commandler.confiler;

import com.elypia.commandler.CommandInput;
import com.elypia.commandler.confiler.reactions.DefaultReactionController;
import com.elypia.commandler.events.CommandEvent;
import com.elypia.commandler.impl.*;
import net.dv8tion.jda.core.events.message.GenericMessageEvent;

import java.util.*;
import java.util.regex.*;

/**
 * If you're configuing Commandler, it's a good idea to extend and {@link Override}
 * the {@link #getPrefix(GenericMessageEvent)} method here to get you going.
 */

public class DefaultConfiler implements IConfiler<GenericMessageEvent> {

    /**
     * The default commands regex, this matches the commands to see if its
     * valid and returns the matches as groups.
     */

    private static final String COMMAND_REGEX = "(?i)\\A(?:\\\\?<@!?%s>\\s{0,2}|\\Q%s\\E)(?<module>[A-Z\\d]+)(?:\\s+(?<command>[A-Z\\d]+))?(?:\\s+(?<params>.+))?\\Z";

    /**
     * The default params regex, this matches every argument in the commands,
     * any comma seperated <strong>args</strong> will be split by comma as a list.
     */

    private static final Pattern PARAM_PATTERN = Pattern.compile("(?:(?:\"(?:\\\\\"|[^\"])*\"|[^\\s,]+)(?:\\s*,\\s*)?)+");

    private static final Pattern ITEM_PATTERN = Pattern.compile("\"(?<quote>(?:\\\\\"|[^\"])*)\"|(?<word>[^\\s,]+)");

    private final String DEFAULT_PREFIX;

    private final String HELP_URL;

    private final DefaultReactionController REACTION_CONTROLLER;

    /**
     * The first time the commands regular expression is queried, we take
     * {@link #COMMAND_REGEX} and insert the bots id and store it here to save
     * us from populating it per commands.
     */

    private String commandRegex;

    /**
     * Instantiate the Default configuration for Commandler with a different prefix.
     *
     * @param prefix The default prefix for the bot.
     */

    public DefaultConfiler(String prefix) {
        this(prefix, null);
    }

    public DefaultConfiler(String prefix, String help) {
        DEFAULT_PREFIX = prefix;
        HELP_URL = help;
        REACTION_CONTROLLER = new DefaultReactionController();
    }

    @Override
    public CommandInput processCommand(GenericMessageEvent event, String content) {
        String id = event.getJDA().getSelfUser().getId();
        String prefix = getPrefix(event);
        String pattern = String.format(COMMAND_REGEX, id, prefix);
        Pattern commandPattern = Pattern.compile(pattern);

        Matcher matcher = commandPattern.matcher(content);

        String module = matcher.group("module");
        String command = matcher.group("command");

        if (!matcher.matches())
            return null;

        List<List<String>> parameters = new ArrayList<>();
        String params = matcher.group("params");

        if (params != null) {
            matcher = PARAM_PATTERN.matcher(params);

            while (matcher.find()) {
                String group = matcher.group();
                Matcher splitMatcher = ITEM_PATTERN.matcher(group);

                List<String> list = new ArrayList<>();
                while (splitMatcher.find()) {
                    String quote = splitMatcher.group("quote");
                    list.add(quote != null ?  quote : splitMatcher.group("word"));
                }

                parameters.add(list);
            }
        }

        return new CommandInput(module, command, parameters);
    }

    /**
     * @param event The message event as provided by JDA.
     * @return The prefix to be used for this particular event.
     */

    @Override
    public String getPrefix(GenericMessageEvent event) {
        return DEFAULT_PREFIX;
    }

    @Override
    public long[] getDevelopers() {
        return new long[0];
    }

    @Override
    public String getHelpUrl(CommandEvent event) {
        return HELP_URL;
    }

    @Override
    public String getHelp(GenericMessageEvent event, String key) {
        return key;
    }

    @Override
    public IReactionController getReactionController() {
        return REACTION_CONTROLLER;
    }
}

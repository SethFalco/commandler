package com.elypia.commandler;

import com.elypia.commandler.annotations.*;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.impl.*;
import com.elypia.commandler.pages.PageBuilder;

import java.util.*;
import java.util.regex.*;

/**
 * If you're configuing Commandler, it's a good idea to extend and {@link Override}
 * methods in the {@link Confiler}, alternatively you can make your own
 * from scratch by making an implementation of {@link IConfiler}.
 */
public class Confiler<C, E, M> implements IConfiler<C, E, M> {

    /**
     * The default commands regex, this matches the commands to see if its
     * valid and returns the matches as groups.
     */
    protected static final String COMMAND_REGEX = "(?i)\\A(?:%s)(?<module>[A-Z\\d]+)(?:\\s+(?<command>[A-Z\\d]+))?(?:\\s+(?<params>.+))?\\Z";

    /**
     * The default params regex, this matches every argument in the commands,
     * any comma seperated <strong>args</strong> will be split by {@link #ITEM_PATTERN} as a list.
     */
    protected static final Pattern PARAM_PATTERN = Pattern.compile("(?:(?:\"(?:\\\\\"|[^\"])*\"|[^\\s,]+)(?:\\s*,\\s*)?)+");

    /**
     * The default item regex, this matches every item within a list of parameters.
     * This is for list parameters as a single parameter can contain multiple items.
     */
    protected static final Pattern ITEM_PATTERN = Pattern.compile("\"(?<quote>(?:\\\\\"|[^\"])*)\"|(?<word>[^\\s,]+)");

    /**
     * The default prefixes used to dictate a user is performing a command. <br>
     * The first prefix in this list is considered the global application default,
     * all other prefixes are alternatives.
     */
    protected final String[] DEFAULT_PREFIXES;

    /**
     * The url to the website help is displayed if configured. <br>
     * @see PageBuilder for generating a website with information
     *      on {@link Module}s and {@link Command}s.
     */
    protected final String HELP_URL;

    protected IMisuseListener misuseListener;

    /**
     * Instantiate the configuration to inject into {@link Commandler}.
     *
     * @param prefixes The default prefixes for the bot, if not prefix
     *                 is passed, then the bot does not require a prefix what so ever.
     */
    public Confiler(String... prefixes) {
        this(prefixes, null);

        // ? By default use the IMisuseListener default implementation
        misuseListener = new IMisuseListener(){};
    }

    /**
     * Instantiate the configuration to inject into {@link Commandler}.
     *
     * @param prefixes The default prefix for the bot.
     * @param help The url to the help documentaton for the bot.
     */
    public Confiler(String[] prefixes, String help) {
        DEFAULT_PREFIXES = prefixes;
        HELP_URL = help;
    }

    /**
     * Break the command down into it's individual components.
     *
     * @param event The event spanwed by the client.
     * @param content The content of the meessage.
     * @return The input the user provided or null if it's not a valid command.
     */
    @Override
    public CommandInput processEvent(Commandler<C, E, M> commandler, E event, String content) {
        StringJoiner joiner = new StringJoiner("|");

        for (String prefix : getPrefixes(commandler, event))
            joiner.add("\\Q" + prefix + "\\E");

        String pattern = String.format(COMMAND_REGEX, joiner.toString());
        Pattern commandPattern = Pattern.compile(pattern);

        Matcher matcher = commandPattern.matcher(content);

        if (!matcher.matches())
            return null;

        String module = matcher.group("module");
        String command = matcher.group("command");
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

        return new CommandInput(commandler, content, module, command, parameters);
    }

    @Override
    public IMisuseListener getMisuseListener() {
        return misuseListener;
    }

    /**
     * @param event The message event provided by the client.
     * @return The prefixes considered acceptable for this event.
     */
    @Override
    public String[] getPrefixes(Commandler commandler, E event) {
        return DEFAULT_PREFIXES;
    }

    @Override
    public String getHelp(Commandler commandler, E event, String key) {
        return key;
    }

    @Override
    public String getHelpUrl(Commandler<C, E, M> commandler, E event) {
        return HELP_URL;
    }
}

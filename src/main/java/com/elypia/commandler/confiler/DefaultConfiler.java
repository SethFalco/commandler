package com.elypia.commandler.confiler;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.regex.Pattern;

public class DefaultConfiler implements Confiler {

    private static final String COMMAND_REGEX = "(?i)^(?:<@!?%s>|%s)\\s{0,2}(?<module>[A-Z]+)(?:\\.(?<submodule>[A-Z]+))?(?: (?<command>[^\\s]+))?(?: (?<params>.*))?";
    private static final Pattern PARAM_PATTERN = Pattern.compile("(?<quotes>\\b(?<=\")(?:\\\\\"|[^\"])*(?=\"))|(?<args>[^\\s\",]+(?:\\s*,\\s*[^\\s\",]+)*)");

    private final String DEFAULT_PREFIX;

    public DefaultConfiler() {
        this("!");
    }

    public DefaultConfiler(String prefix) {
        DEFAULT_PREFIX = prefix;
    }

    @Override
    public Pattern getCommandRegex(MessageReceivedEvent event) {
        String id = event.getJDA().getSelfUser().getId();
        String prefix = getPrefix(event);

        String pattern = String.format(COMMAND_REGEX, id, prefix);
        return Pattern.compile(pattern);
    }

    @Override
    public Pattern getParamRegex(MessageReceivedEvent event) {
        return PARAM_PATTERN;
    }

    @Override
    public String getPrefix(MessageReceivedEvent event) {
        return DEFAULT_PREFIX;
    }
}

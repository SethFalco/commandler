package org.elypia.commandler.utils;

import java.util.Map;
import java.util.regex.Pattern;

/**
 * Generic utilities for which can be used in chat.
 * Most utilities provided are required by Commandler.
 */
public final class ChatUtils {

    private static final Pattern SPACE_SPLITTER = Pattern.compile("\\s+");

    /** Many chat services use two regional indicators to represent a country. */
    private static final Map<String, String> regionalIndicators = Map.ofEntries(
        Map.entry("A", "\uD83C\uDDE6"),
        Map.entry("B", "\uD83C\uDDE7"),
        Map.entry("C", "\uD83C\uDDE8"),
        Map.entry("D", "\uD83C\uDDE9"),
        Map.entry("E", "\uD83C\uDDEA"),
        Map.entry("F", "\uD83C\uDDEB"),
        Map.entry("G", "\uD83C\uDDEC"),
        Map.entry("H", "\uD83C\uDDED"),
        Map.entry("I", "\uD83C\uDDEE"),
        Map.entry("J", "\uD83C\uDDEF"),
        Map.entry("K", "\uD83C\uDDF0"),
        Map.entry("L", "\uD83C\uDDF1"),
        Map.entry("M", "\uD83C\uDDF2"),
        Map.entry("N", "\uD83C\uDDF3"),
        Map.entry("O", "\uD83C\uDDF4"),
        Map.entry("P", "\uD83C\uDDF5"),
        Map.entry("Q", "\uD83C\uDDF6"),
        Map.entry("R", "\uD83C\uDDF7"),
        Map.entry("S", "\uD83C\uDDF8"),
        Map.entry("T", "\uD83C\uDDF9"),
        Map.entry("U", "\uD83C\uDDFA"),
        Map.entry("V", "\uD83C\uDDFB"),
        Map.entry("W", "\uD83C\uDDFC"),
        Map.entry("X", "\uD83C\uDDFD"),
        Map.entry("Y", "\uD83C\uDDFE"),
        Map.entry("Z", "\uD83C\uDDFF")
    );

    private ChatUtils() {
        // Do nothing
    }

    /**
     * Replace all upper case characters with regional indicators
     * characters instead.
     *
     * @param input The source string to replace from.
     * @return The new string with all upper case characters replaced.
     */
    public static String replaceWithIndictors(String input) {
        for (Map.Entry<String, String> entry : regionalIndicators.entrySet())
            input = input.replace(entry.getKey(), entry.getValue());

        return input;
    }

    /**
     * @param body The content of a message.
     * @return A string array of each word in the string provided.
     */
    public static String[] splitSpaces(final String body) {
        return splitSpaces(body, 0);
    }

    /**
     * @param body The content of a message.
     * @param limit The max number of words we're interested in.
     * @return A string array of each word in the string provided
     * up to the limit provided.
     */
    public static String[] splitSpaces(final String body, final int limit) {
        return SPACE_SPLITTER.split(body, limit);
    }
}

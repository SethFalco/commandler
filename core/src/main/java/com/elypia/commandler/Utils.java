package com.elypia.commandler;

import java.util.regex.Pattern;

public final class Utils {

    private static final Pattern SPACE_SPLITTER = Pattern.compile("\\s+");

    public static String[] splitSpaces(final String body) {
        return splitSpaces(body, 0);
    }

    public static String[] splitSpaces(final String body, final int limit) {
        return SPACE_SPLITTER.split(body, limit);
    }
}

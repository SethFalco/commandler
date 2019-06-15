package com.elypia.commandler.utils;

import java.util.regex.Pattern;

public final class Utils {

    public static final String EFFECTIVELY_NULL = "\n\t\t\n\t\t\n\uE000\uE001\uE002\n\t\t\t\t\n";

    private static final Pattern SPACE_SPLITTER = Pattern.compile("\\s+");

    public static boolean isEffectivelyNull(String value) {
        return value == EFFECTIVELY_NULL;
    }

    public static String[] splitSpaces(final String body) {
        return splitSpaces(body, 0);
    }

    public static String[] splitSpaces(final String body, final int limit) {
        return SPACE_SPLITTER.split(body, limit);
    }
}

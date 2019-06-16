package com.elypia.commandler.utils;

public final class AnnoUtils {

    public static final String EFFECTIVELY_NULL = "\n\t\t\n\t\t\n\uE000\uE001\uE002\n\t\t\t\t\n";

    public static boolean effectivelyNull(String value) {
        return value == EFFECTIVELY_NULL;
    }

    public static String effectivelyNullElse(String value) {
        return effectivelyNullElse(value, null);
    }

    /**
     * @param value The value to check if {@link #effectivelyNull(String)}
     * @param elseValue The value to set if it is {@link #effectivelyNull(String)}.
     * @return The value provided provided if not null, else the else parameter.
     */
    public static String effectivelyNullElse(String value, String elseValue) {
        return (effectivelyNull(value)) ? elseValue : value;
    }

    public static boolean effectivelyNull(String[] array) {
        return array.length == 1 && effectivelyNull(array[0]);
    }

    public static String[] effectivelyNullElse(String[] value) {
        return effectivelyNullElse(value, null);
    }

    public static String[] effectivelyNullElse(String[] value, String[] elseValue) {
        return (effectivelyNull(value)) ? elseValue : value;
    }
}

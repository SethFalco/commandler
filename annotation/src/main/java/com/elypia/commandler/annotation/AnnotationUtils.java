package com.elypia.commandler.annotation;

public final class AnnotationUtils {

    public static final String EFFECTIVELY_NULL = "\n\t\t\n\t\t\n\uE000\uE001\uE002\n\t\t\t\t\n";

    private AnnotationUtils() {
        // Do nothing
    }

    public static boolean isEffectivelyNull(String value) {
        return value.equals(EFFECTIVELY_NULL);
    }

    public static String ifEffectivelyNull(String value) {
        return ifEffectivelyNull(value, null);
    }

    /**
     * @param value The key to check if {@link #isEffectivelyNull(String)}
     * @param elseValue The key to set if it is {@link #isEffectivelyNull(String)}.
     * @return The key provided provided if not null, else the else parameter.
     */
    public static String ifEffectivelyNull(String value, String elseValue) {
        return (isEffectivelyNull(value)) ? elseValue : value;
    }

    public static boolean isEffectivelyNull(String[] array) {
        return array.length == 1 && isEffectivelyNull(array[0]);
    }

    public static String[] ifEffectivelyNull(String[] value) {
        return ifEffectivelyNull(value, null);
    }

    public static String[] ifEffectivelyNull(String[] value, String[] elseValue) {
        return (isEffectivelyNull(value)) ? elseValue : value;
    }
}

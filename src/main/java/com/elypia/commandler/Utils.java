package com.elypia.commandler;

public final class Utils {

    private Utils() {
        // Can't construct
    }

    public static void log(String message, Object...args) {
        System.err.printf("WARNING: " + message + "\n", args);
    }
}

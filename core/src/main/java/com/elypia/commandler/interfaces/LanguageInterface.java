package com.elypia.commandler.interfaces;

import com.elypia.commandler.CommandlerEvent;

import java.util.*;

public interface LanguageInterface {

    default String get(String key) {
        return get(key, Map.of());
    }

    default <T> String get(String key, Map<String, T> params) {
        return get(null, key, params);
    }

    default String get(CommandlerEvent<?, ?> event, String key) {
        return get(event, key, Map.of());
    }

    <T> String get(CommandlerEvent<?, ?> event, String key, Map<String, T> params);
    Locale getLocale(CommandlerEvent<?, ?> event);
    Locale[] getSupportedLocales();
}

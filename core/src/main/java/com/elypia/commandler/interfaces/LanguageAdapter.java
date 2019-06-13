package com.elypia.commandler.interfaces;

import java.util.*;

public interface LanguageAdapter<S> {

    default String get(String key) {
        return get(key, Map.of());
    }

    default <T> String get(String key, Map<String, T> params) {
        return get(null, key, params);
    }

    default String get(S source, String key) {
        return get(source, key, Map.of());
    }

    <T> String get(S source, String key, Map<String, T> params);
    Locale getLocale(S source);
    Locale[] getSupportedLocales();
}

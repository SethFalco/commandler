package com.elypia.commandler.interfaces;

import java.util.Map;

public interface IScriptEngine<S> {

    default String get(String script) {
        return get(script, Map.of());
    }

    default <T> String  get(String script, Map<String, T> params) {
        return get(null, script, params);
    }

    default String get(S source, String script) {
        return get(source, script, Map.of());
    }

    <T> String get(S source, String script, Map<String, T> params);
}

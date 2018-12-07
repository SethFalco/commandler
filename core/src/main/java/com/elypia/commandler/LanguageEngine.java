package com.elypia.commandler;

import java.util.Map;

/**
 * The {@link LanguageEngine} is used in order to get and display scripts
 * whereever text may be required. This could be as a message, on a website
 * or anything that doesn't have debugging intent.
 */
public class LanguageEngine<E> {

    public <T> String getScript(E event, String script, Map<String, T> params) {
        for (var entry : params.entrySet())
            script = script.replace("${" + entry.getKey() + "}", entry.getValue().toString());

        return script;
    }

    public String getScript(E event, String script, Object... args) {
        return String.format(script, args);
    }
}

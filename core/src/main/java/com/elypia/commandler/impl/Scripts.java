package com.elypia.commandler.impl;

import com.elypia.commandler.interfaces.IScripts;

import java.util.Map;

/**
 * The {@link Scripts} is used in order to get and display scripts
 * whereever text may be required. This could be as a message, on a website
 * or anything that doesn't have debugging intent.
 */
public class Scripts<S> implements IScripts<S> {

    private static final String[] languages = {"en"};

    @Override
    public <T> String get(S event, String script, Map<String, T> params) {
        if (params != null) {
            for (var entry : params.entrySet())
                script = script.replace("${" + entry.getKey() + "}", entry.getValue().toString());
        }

        return script;
    }

    @Override
    public String getLanguage(S source) {
        return languages[0];
    }

    @Override
    public String[] getSupportedLanguages() {
        return languages;
    }
}

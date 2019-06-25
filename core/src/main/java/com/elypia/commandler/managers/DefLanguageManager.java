package com.elypia.commandler.managers;

import com.elypia.commandler.CommandlerEvent;

import java.util.*;

/**
 * The {@link DefLanguageManager} is used in order to get and display scripts
 * whereever text may be required. This could be as a message, on a website
 * or anything that doesn't have debugging intent.
 */
// TODO: Give this a better name
public class DefLanguageManager<S> implements com.elypia.commandler.interfaces.LanguageManager {

    private static final Locale[] locales = {Locale.ENGLISH};

    @Override
    public <T> String get(CommandlerEvent<?> event, String key, Map<String, T> params) {
        String script = ResourceBundle.getBundle("language", getLocale(event)).getString(key);

        if (params != null) {
            for (var entry : params.entrySet())
                script = script.replace("${" + entry.getKey() + "}", entry.getValue().toString());
        }

        return script;
    }

    @Override
    public Locale getLocale(CommandlerEvent<?> event) {
        return locales[0];
    }

    @Override
    public Locale[] getSupportedLocales() {
        return locales;
    }
}

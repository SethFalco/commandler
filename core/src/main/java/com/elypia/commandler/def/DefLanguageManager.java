package com.elypia.commandler.def;

import com.elypia.commandler.CommandlerEvent;
import com.elypia.commandler.interfaces.LanguageManager;

import java.util.*;

/**
 * The {@link DefLanguageManager} is used in order to get and display scripts
 * whereever text may be required. This could be as a message, on a website
 * or anything that doesn't have debugging intent.
 */
public class DefLanguageManager<S> implements LanguageManager {

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

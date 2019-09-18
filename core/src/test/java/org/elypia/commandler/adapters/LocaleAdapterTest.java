package org.elypia.commandler.adapters;

import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

public class LocaleAdapterTest {

    @Test
    public void findLocaleByLanguage() {
        LocaleAdapter adapter = new LocaleAdapter();
        Locale locale = adapter.adapt("English");
        assertEquals("en", locale.getLanguage());
    }

    @Test
    public void findLocaleByCountry() {
        LocaleAdapter adapter = new LocaleAdapter();
        Locale locale = adapter.adapt("United Kingdom");
        assertEquals("en", locale.getLanguage());
    }

    @Test
    public void findLocaleByTag() {
        LocaleAdapter adapter = new LocaleAdapter();
        Locale locale = adapter.adapt("en-GB");
        assertEquals("en", locale.getLanguage());
    }

    @Test
    public void findLocaleByEmote() {
        LocaleAdapter adapter = new LocaleAdapter();
        Locale locale = adapter.adapt("\uD83C\uDDEC\uD83C\uDDE7");
        assertEquals("en", locale.getLanguage());
    }

    @Test
    public void findLocaleByIso3() {
        LocaleAdapter adapter = new LocaleAdapter();
        Locale locale = adapter.adapt("pol");
        assertEquals("pl", locale.getLanguage());
    }

    @Test
    public void testNull() {
        LocaleAdapter adapter = new LocaleAdapter();

        assertAll("Check if all these return null.",
            () -> assertNull(adapter.adapt("invalid")),
            () -> assertNull(adapter.adapt("this place doesn't exist")),
            () -> assertNull(adapter.adapt("en-nou"))
        );
    }
}

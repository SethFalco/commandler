/*
 * Copyright 2019-2020 Elypia CIC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.elypia.commandler.adapters;

import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author seth@elypia.org (Seth Falco)
 */
public class LocaleAdapterTest {

    @Test
    public void findLocaleByLanguage() {
        LocaleAdapter adapter = new LocaleAdapter();

        Locale expected = Locale.ENGLISH;
        Locale actual = adapter.adapt("English");

        assertEquals(expected, actual);
    }

    @Test
    public void findLocaleByOtherLanguage() {
        LocaleAdapter adapter = new LocaleAdapter(Locale.FRANCE);

        Locale expected = Locale.ENGLISH;
        Locale actual = adapter.adapt("Anglais");

        assertEquals(expected, actual);
    }

    @Test
    public void findLocaleOfOtherLanguage() {
        LocaleAdapter adapter = new LocaleAdapter();

        Locale expected = Locale.FRENCH;
        Locale actual = adapter.adapt("Français");

        assertEquals(expected, actual);
    }

    @Test
    public void findLocaleByTag() {
        LocaleAdapter adapter = new LocaleAdapter();

        Locale expected = Locale.UK;
        Locale actual = adapter.adapt("en-GB");

        assertEquals(expected, actual);
    }

    @Test
    public void findLocaleByTagUnderscore() {
        LocaleAdapter adapter = new LocaleAdapter();

        Locale expected = Locale.FRANCE;
        Locale actual = adapter.adapt("fr_FR");

        assertEquals(expected, actual);
    }

    /**
     * Internally has a flag to detect if it's an emote.
     * This will be converted to the country code and only
     * compared to the ISO codes for country.
     */
    @Test
    public void findLocaleByEmote() {
        LocaleAdapter adapter = new LocaleAdapter();

        Locale expected = Locale.FRANCE;
        Locale actual = adapter.adapt("\uD83C\uDDEB\uD83C\uDDF7");

        assertEquals(expected, actual);
    }

    @Test
    public void findLocaleByIso3() {
        LocaleAdapter adapter = new LocaleAdapter();

        Locale expected = new Locale("pl", "PL");
        Locale actual = adapter.adapt("pol");

        assertEquals(expected, actual);
    }

    /**
     * When searching for locale, but only a single item is
     * provided such as "fr", we should return the more specific
     * match first rather than first match.
     */
    @Test
    public void findLocaleByIsoByRegionWithMatchingLanguage() {
        LocaleAdapter adapter = new LocaleAdapter();

        Locale expected = Locale.FRENCH;
        Locale actual = adapter.adapt("fr");

        assertEquals(expected, actual);
    }

    /**
     * When searching for a locale by country, we should prefer
     * the country the iso code between language and country are the same.
     */
    @Test
    public void findLocaleByCountryNameWithMatchingLanguage() {
        LocaleAdapter adapter = new LocaleAdapter();

        Locale expected = Locale.FRANCE;
        Locale actual = adapter.adapt("france");

        assertEquals(expected, actual);
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

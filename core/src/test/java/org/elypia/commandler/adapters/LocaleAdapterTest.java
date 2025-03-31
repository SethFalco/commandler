/*
 * Copyright 2019-2025 Seth Falco and Commandler Contributors
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Locale;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * @author seth@falco.fun (Seth Falco)
 */
public class LocaleAdapterTest {

    @Test
    public void findLocaleByLanguage() {
        LocaleAdapter adapter = new LocaleAdapter(Locale.US);

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
        LocaleAdapter adapter = new LocaleAdapter(Locale.US);

        Locale expected = Locale.FRENCH;
        Locale actual = adapter.adapt("Français");

        assertEquals(expected, actual);
    }

    @Test
    public void findLocaleByTag() {
        LocaleAdapter adapter = new LocaleAdapter(Locale.US);

        Locale expected = Locale.UK;
        Locale actual = adapter.adapt("en-GB");

        assertEquals(expected, actual);
    }

    @Test
    public void findLocaleByTagUnderscore() {
        LocaleAdapter adapter = new LocaleAdapter(Locale.US);

        Locale expected = Locale.FRANCE;
        Locale actual = adapter.adapt("fr_FR");

        assertEquals(expected, actual);
    }

    /**
     * When searching for locale, but only a single item is
     * provided such as "fr", we should return the more specific
     * match first rather than first match.
     */
    @Test
    public void findLocaleByIsoByRegionWithMatchingLanguage() {
        LocaleAdapter adapter = new LocaleAdapter(Locale.US);

        Locale expected = Locale.FRENCH;
        Locale actual = adapter.adapt("fr");

        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @ValueSource(strings = {"invalid", "this place doesn't exist", "en-nou"})
    public void testNull(String value) {
        LocaleAdapter adapter = new LocaleAdapter();
        assertNull(adapter.adapt(value));
    }
}

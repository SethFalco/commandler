/*
 * Copyright 2019-2019 Elypia CIC
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

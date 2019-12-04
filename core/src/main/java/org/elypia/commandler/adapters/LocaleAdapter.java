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

import org.elypia.commandler.api.Adapter;
import org.elypia.commandler.event.ActionEvent;
import org.elypia.commandler.metadata.MetaParam;
import org.elypia.commandler.utils.ChatUtils;
import org.slf4j.*;

import javax.inject.Singleton;
import java.util.*;

/**
 * @author seth@elypia.org (Seth Falco)
 */
@Singleton
public class LocaleAdapter implements Adapter<Locale> {

    private static final Logger logger = LoggerFactory.getLogger(LocaleAdapter.class);

    private static final Locale[] LOCALES = Locale.getAvailableLocales();

    @Override
    public Locale adapt(String input, Class<? extends Locale> type, MetaParam metaParam, ActionEvent<?, ?> event) {
        Objects.requireNonNull(input);

        // If using regional indicators, for example emojis, make them the textual equivilent.
        String country = null;
        if (input.length() == 4)
            country = ChatUtils.replaceFromIndicators(input);

        Locale temp = null;

        for (Locale locale : LOCALES) {
            if (input.equalsIgnoreCase(locale.toLanguageTag()))
                return locale;

            if (!locale.getCountry().isBlank()) {
                String c = locale.getCountry();
                String dc = locale.getDisplayCountry(Locale.US);
                String dcl = locale.getDisplayCountry(locale);

                // Checks flag emote.
                if (input.equalsIgnoreCase(c) || (country != null && country.equalsIgnoreCase(c)))
                    temp = locale;

                if (input.equalsIgnoreCase(dc) || input.equalsIgnoreCase(dcl))
                    return locale;

                try {
                    if (input.equalsIgnoreCase(locale.getISO3Country()))
                        return locale;
                } catch (MissingResourceException ex) {
                    // Do nothing, there is nothing wrong with this.
                }
            }

            String l = locale.getLanguage();
            String dl = locale.getDisplayLanguage(Locale.US);
            String dll = locale.getDisplayLanguage(locale);
            String lc = locale.getISO3Language();

            if (input.equalsIgnoreCase(l))
                temp = locale;

            if (input.equalsIgnoreCase(dl) || input.equalsIgnoreCase(dll) || input.equalsIgnoreCase(lc))
                return locale;
        }

        return temp;
    }
}

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

import org.apache.deltaspike.core.api.message.LocaleResolver;
import org.elypia.commandler.annotation.stereotypes.ParamAdapter;
import org.elypia.commandler.api.Adapter;
import org.elypia.commandler.event.ActionEvent;
import org.elypia.commandler.metadata.MetaParam;
import org.elypia.commandler.utils.ChatUtils;
import org.slf4j.*;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.util.*;

/**
 * @author seth@elypia.org (Seth Falco)
 */
@RequestScoped
@ParamAdapter(Locale.class)
public class LocaleAdapter implements Adapter<Locale> {

    private static final Logger logger = LoggerFactory.getLogger(LocaleAdapter.class);

    private static final Locale[] LOCALES = Locale.getAvailableLocales();

    /** The length of a country flag emoji string, available on most operation systems or social media. */
    private static final int COUNTRY_FLAG_EMOJI_LENGTH = 4;

    /** The max number of acceptable strings we're expecting to have. */
    private static final int ACCEPTABLE_STRINGS_INITIAL_SIZE = 11;

    private final Locale currentLocale;

    public LocaleAdapter() {
        this(Locale.getDefault());
    }

    @Inject
    public LocaleAdapter(LocaleResolver localeResolver) {
        this(localeResolver.getLocale());
    }

    public LocaleAdapter(Locale locale) {
        this.currentLocale = locale;
    }

    @Override
    public Locale adapt(final String input, Class<? extends Locale> type, MetaParam metaParam, ActionEvent<?, ?> event) {
        Objects.requireNonNull(input);

        String query = input.replace('_', '-');

        String countryEmoji = null;

        if (query.length() == COUNTRY_FLAG_EMOJI_LENGTH)
            countryEmoji = ChatUtils.replaceFromIndicators(query);

        Set<Locale> candidates = null;
        Locale languageLocale = null;
        Locale countryLocale = null;

        for (Locale locale : LOCALES) {
            if (query.equalsIgnoreCase(locale.toLanguageTag()))
                return locale;

            if (candidates == null)
                candidates = new HashSet<>(ACCEPTABLE_STRINGS_INITIAL_SIZE);

            Set<String> acceptableStrings = new HashSet<>();
            acceptableStrings.add(locale.getLanguage());
            acceptableStrings.add(locale.getDisplayLanguage(Locale.US));
            acceptableStrings.add(locale.getDisplayLanguage(locale));
            acceptableStrings.add(locale.getDisplayLanguage(currentLocale));
            acceptableStrings.add(locale.getISO3Language());

            if (acceptableStrings.stream().anyMatch(query::equalsIgnoreCase)) {
                candidates.add(locale);
                languageLocale = locale;
            }

            acceptableStrings.clear();

            if (!locale.getCountry().isBlank()) {
                acceptableStrings.add(locale.getCountry());
                acceptableStrings.add(locale.getDisplayCountry(Locale.US));
                acceptableStrings.add(locale.getDisplayCountry(locale));
                acceptableStrings.add(locale.getDisplayCountry(currentLocale));

                try {
                    acceptableStrings.add(locale.getISO3Country());
                } catch (MissingResourceException ex) {
                    // Do nothing, there's nothing wrong with this.
                }
            }

            if (acceptableStrings.stream().anyMatch(query::equalsIgnoreCase) || locale.getCountry().equals(countryEmoji)) {
                candidates.add(locale);
                countryLocale = locale;
            }
        }

        if (countryLocale == languageLocale)
            return countryLocale;

        if (languageLocale != null && countryLocale == null) {
            String ll = languageLocale.getLanguage();
            Optional<Locale> test = candidates.stream()
                .filter((l) -> ll.equalsIgnoreCase(l.getLanguage()) && l.getCountry().isBlank())
                .findAny();

            if (test.isPresent())
                return test.get();
        }

        if (languageLocale == null) {
            String cc = countryLocale.getCountry();
            Optional<Locale> test = candidates.stream()
                .filter((l) -> l.getLanguage().equalsIgnoreCase(cc) && l.getCountry().equals(cc))
                .findAny();

            if (test.isPresent())
                return test.get();
        }

        return (countryLocale != null) ? countryLocale : languageLocale;
    }
}

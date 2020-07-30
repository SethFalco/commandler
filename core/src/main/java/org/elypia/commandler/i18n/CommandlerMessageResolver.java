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

package org.elypia.commandler.i18n;

import org.apache.deltaspike.core.api.message.*;
import org.elypia.commandler.annotation.Property;
import org.elypia.commandler.metadata.MetaComponent;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.*;

/**
 * Used by Commandler to localize metadata for
 * {@link MetaComponent}s or {@link Property properties}
 * with {@link Property#i18n()} set to true.
 *
 * @author seth@elypia.org (Seth Falco)
 * @since 4.0.1
 */
@ApplicationScoped
public class CommandlerMessageResolver {

    /** Messages to load by default. */
    private static final String[] DEFAULT_MESSAGES = {
        "org.elypia.commandler.i18n.GroupMessages"
    };

    /** Configuration for Commandler's i18n handling. */
    private final InternationalizationConfig i18nConfig;

    private final MessageContext messageContext;

    @Inject
    public CommandlerMessageResolver(InternationalizationConfig i18nConfig, MessageContext messageContext) {
        this.i18nConfig = Objects.requireNonNull(i18nConfig);
        this.messageContext = Objects.requireNonNull(messageContext);

        List<String> messages = new ArrayList<>(i18nConfig.getMessageBundles());
        messages.addAll(Arrays.asList(DEFAULT_MESSAGES));

        messageContext.messageSource(messages.toArray(String[]::new));
    }

    /**
     * If the key starts and ends with {} then it'll be searched
     * in the {@link InternationalizationConfig#getMessageBundles()} path.
     *
     * If not then the string will be interpolated and returned literally.
     *
     * @param key The resource bundle key or literal string localize.
     * @return The localized string depending on the {@link LocaleResolver}.
     */
    public String getMessage(String key) {
        return getMessage(key, null);
    }

    /**
     * If the key starts and ends with {} then it'll be searched
     * in the {@link InternationalizationConfig#getMessageBundles()} path.
     *
     * If not then the string will be interpolated and returned literally.
     *
     * @param key The resource bundle key or literal string localize.
     * @param locale Override the default locale resolver implementation to use this locale.
     * @return The localized string in the locale specified.
     */
    public String getMessage(String key, Locale locale) {
        Message message = messageContext.message();
        Message template = message.template(key);
        return template.toString();
    }
}

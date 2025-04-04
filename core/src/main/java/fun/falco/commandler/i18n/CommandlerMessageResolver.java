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

package fun.falco.commandler.i18n;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.deltaspike.core.api.message.LocaleResolver;
import org.apache.deltaspike.core.api.message.Message;
import org.apache.deltaspike.core.api.message.MessageContext;

import fun.falco.commandler.annotation.Property;
import fun.falco.commandler.metadata.MetaComponent;

/**
 * Used by Commandler to localize metadata for {@link MetaComponent}s or
 * {@link Property properties} with {@link Property#i18n()} set to true.
 *
 * @author seth@falco.fun (Seth Falco)
 * @since 4.0.1
 */
@ApplicationScoped
public class CommandlerMessageResolver {

    /** Messages to load by default. */
    private static final String[] DEFAULT_MESSAGES = {
        "fun.falco.commandler.i18n.GroupMessages"
    };

    private final MessageContext messageContext;

    @Inject
    public CommandlerMessageResolver(InternationalizationConfig i18nConfig, MessageContext messageContext) {
        this.messageContext = Objects.requireNonNull(messageContext);

        List<String> messages = new ArrayList<>(i18nConfig.getMessageBundles());
        messages.addAll(Arrays.asList(DEFAULT_MESSAGES));

        messageContext.messageSource(messages.toArray(String[]::new));
    }

    /**
     * If the key starts and ends with {} then it'll be searched in the
     * {@link InternationalizationConfig#getMessageBundles()} path.
     *
     * <p>If not then the string will be interpolated and returned
     * literally.</p>
     *
     * @param key Resource bundle key or literal string localize.
     * @return Localized string depending on the {@link LocaleResolver}.
     */
    public String getMessage(String key) {
        return getMessage(key, null);
    }

    /**
     * If the key starts and ends with {} then it'll be searched in the
     * {@link InternationalizationConfig#getMessageBundles()} path.
     *
     * <p>If not then the string will be interpolated and returned
     * literally.</p>
     *
     * @param key Resource bundle key or literal string localize.
     * @param locale
     *     Override the default locale resolver implementation to use this
     *     locale.
     * @return Localized string in the locale specified.
     */
    public String getMessage(String key, Locale locale) {
        Message message = messageContext.message();
        Message template = message.template(key);
        return template.toString();
    }
}

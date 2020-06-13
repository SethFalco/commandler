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

import org.apache.deltaspike.core.api.config.*;
import org.apache.deltaspike.core.api.message.MessageBundle;

/**
 * Configuration for internationalization of Commandler.
 *
 * @author seth@elypia.org (Seth Falco)
 * @since 4.0.1
 */
@Configuration(prefix = "commandler.i18n.")
public interface InternationalizationConfig {

    /**
     * @return The location in the classpath to find the
     * {@link MessageBundle}s for Commandler.
     */
    @ConfigProperty(name = "message-bundle")
    String getMessageBundle();
}

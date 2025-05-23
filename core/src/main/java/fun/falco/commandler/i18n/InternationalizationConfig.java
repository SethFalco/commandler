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

import java.util.List;

import org.apache.deltaspike.core.api.config.ConfigProperty;
import org.apache.deltaspike.core.api.config.Configuration;
import org.apache.deltaspike.core.api.message.MessageBundle;

/**
 * Configuration for internationalization of Commandler.
 *
 * @author seth@falco.fun (Seth Falco)
 * @since 4.0.1
 */
@Configuration(prefix = "commandler.i18n.")
public interface InternationalizationConfig {

    /**
     * @return
     *     Location in the classpath to find the {@link MessageBundle}s for Commandler.
     */
    @ConfigProperty(name = "message-bundles")
    List<String> getMessageBundles();
}

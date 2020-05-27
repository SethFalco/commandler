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

package org.elypia.commandler.dispatchers;

import org.apache.deltaspike.core.api.config.*;
import org.elypia.commandler.config.PatternConverter;

import java.util.List;
import java.util.regex.Pattern;

@Configuration(prefix = "commandler.standard-dispatcher.")
public interface StandardDispatcherConfig {

    @ConfigProperty(name = "prefixes", defaultValue = "$")
    List<String> getPrefixes();

    @ConfigProperty(name = "delimeter", defaultValue = "\\s+", converter = PatternConverter.class)
    Pattern getDelimeter();
}

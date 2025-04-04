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

package fun.falco.commandler.dispatchers.standard;

import java.util.List;
import java.util.regex.Pattern;

import org.apache.deltaspike.core.api.config.ConfigProperty;
import org.apache.deltaspike.core.api.config.Configuration;
import org.elypia.converters4deltaspike.PatternConverter;

/**
 * The configuration for the {@link StandardDispatcher}.
 * This allows modifying how standard commands are processed.
 *
 * @author seth@falco.fun (Seth Falco)
 */
@Configuration(prefix = "commandler.standard-dispatcher.")
public interface StandardDispatcherConfig {

    /**
     * @return Prefixes that infer that the message receives was a command.
     */
    @ConfigProperty(name = "prefixes", defaultValue = "$")
    List<String> getPrefixes();

    /**
     * @return
     *     Delimiter to separate a {@link StandardController#value() prefix}
     *     from an {@link StandardCommand#value() alias}.
     */
    @ConfigProperty(name = "delimiter", defaultValue = "\\s+", converter = PatternConverter.class)
    Pattern getDelimiter();
}

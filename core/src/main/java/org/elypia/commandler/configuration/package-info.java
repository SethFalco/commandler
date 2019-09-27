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

/**
 * Contains all configuration code for Commandler, this primiarly
 * integrates with {@link org.apache.commons.configuration2.Configuration}
 * which handles most of the work for us, this just abstracts it to centralize
 * the code and reduce depend on this abstraction instead of the concrete classes
 * in case we change what configuration library we're using.
 */
package org.elypia.commandler.configuration;

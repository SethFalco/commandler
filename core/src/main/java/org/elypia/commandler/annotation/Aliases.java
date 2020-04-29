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

package org.elypia.commandler.annotation;

import org.elypia.commandler.dispatchers.StandardDispatcher;

import java.lang.annotation.*;

/**
 * Denotes that this is a module or command that can be accessed
 * under aliases.
 *
 * @author seth@elypia.org (Seth Falco)
 */
@PropertyWrapper(type = StandardDispatcher.class)
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Aliases {

    /**
     * @return The aliases for a command to execute this command.
     */
    @Property(key = "aliases")
    String[] value();
}

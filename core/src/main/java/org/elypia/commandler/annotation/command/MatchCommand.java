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

package org.elypia.commandler.annotation.command;

import org.elypia.commandler.annotation.*;
import org.elypia.commandler.dispatchers.MatchDispatcher;

import java.lang.annotation.*;

/**
 * @author seth@elypia.org (Seth Falco)
 */
@Command
@PropertyWrapper(type = MatchDispatcher.class)
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MatchCommand {

    /**
     * @return The regular expression that will be used to
     * match portions of the event.
     */
    @Property(key = "pattern", i18n = true, isPublic = true, displayName = "Pattern")
    String value();
}

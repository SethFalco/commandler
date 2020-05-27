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

import org.elypia.commandler.adapters.EnumAdapter;
import org.elypia.commandler.dispatchers.StandardDispatcher;

import java.lang.annotation.*;

/**
 * Allows enums to specify all acceptable inputs that may
 * result in that enum constant.
 *
 * This is used by the {@link EnumAdapter}.
 *
 * @author seth@elypia.org (Seth Falco)
 */
@PropertyWrapper(type = StandardDispatcher.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface StringValues {

    /**
     * @return Any values that when compared against this enum, mean this value.
     */
    String[] value();

    /**
     * @return If the check should be case sensitive.
     */
    boolean isCaseSensitive() default false;
}

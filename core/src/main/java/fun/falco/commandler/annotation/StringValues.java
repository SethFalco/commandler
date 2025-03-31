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

package fun.falco.commandler.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import fun.falco.commandler.adapters.EnumAdapter;
import fun.falco.commandler.dispatchers.standard.StandardDispatcher;

/**
 * Allows enums to specify all acceptable inputs that may result in that enum
 * constant during parameter parsing.
 *
 * <p>Used by {@link EnumAdapter}.</p>
 *
 * @author seth@falco.fun (Seth Falco)
 * @since 4.0.1
 */
@PropertyWrapper(type = StandardDispatcher.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
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

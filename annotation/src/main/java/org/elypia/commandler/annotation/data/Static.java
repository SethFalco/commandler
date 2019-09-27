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

package org.elypia.commandler.annotation.data;

import org.elypia.commandler.annotation.properties.Aliases;
import org.elypia.commandler.dispatchers.StandardDispatcher;

import java.lang.annotation.*;

/**
 * A static commands is a commands which can be done globally,
 * for example the {@link StandardDispatcher} uses this property
 * to determine if a {@link Command} really needs the {@link Aliases alias}
 * of it's parent {@link Controller} before it can be performed.
 *
 * Different Dispatchers may choose to treat this differnetly.
 *
 * @author seth@elypia.org (Syed Shah)
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Static {

}

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

import org.elypia.commandler.annotation.AnnotationUtils;

import java.lang.annotation.*;

/**
 * The parameter annotation allows us to give parameters for commands
 * a name and short description for what the parameter is or
 * what you need.
 *
 * @author seth@elypia.org (Syed Shah)
 */
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Param {

	/** Set the default key to a literal string.*/
	String defaultValue() default AnnotationUtils.EFFECTIVELY_NULL;
}


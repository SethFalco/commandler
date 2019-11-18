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

package org.elypia.commandler.annotation;

import org.elypia.commandler.Commandler;

import java.lang.annotation.*;

/**
 * A {@link ControllerData} in {@link Commandler} is a subset of {@link CommandData}s
 * and how all {@link CommandData}s should be registered. <br>
 * See {@link Static} and {@link Default} for making simple commands
 * in {@link org.elypia.commandler.api.Controller}s.
 *
 * A {@link ControllerData} can be thought of as a module of commands.
 * A {@link CommandData} can be thought of as a single command.
 *
 * @author seth@elypia.org (Seth Falco)
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ControllerData {

	/**
	 * @return The group this module belongs too, this is used for
	 * navigating the help menu.
	 */
	String group() default AnnotationUtils.EFFECTIVELY_NULL;

	/**
	 * @return If true the module will be hidden from help commands
	 * and documentation.
	 */
	boolean hidden() default false;
}

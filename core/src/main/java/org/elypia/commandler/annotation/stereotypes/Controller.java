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

package org.elypia.commandler.annotation.stereotypes;

import org.elypia.commandler.Commandler;
import org.elypia.commandler.annotation.Command;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Stereotype;
import java.lang.annotation.*;

/**
 * A {@link Controller} in {@link Commandler} is a subset of {@link Command}s
 * and how all {@link Command}s should be registered. <br>
 *
 * A {@link Controller} can be thought of as a module of commands.
 * A {@link Command} can be thought of as a single command.
 *
 * @author seth@elypia.org (Seth Falco)
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@ApplicationScoped
@Stereotype
public @interface Controller {

	/**
	 * @return If true the module will be hidden from help commands
	 * and documentation.
	 */
	boolean hidden() default false;

	/**
	 * @return Related {@link Controller}s that users may be interested in.
	 */
	Class<?>[] seeAlso() default {};
}

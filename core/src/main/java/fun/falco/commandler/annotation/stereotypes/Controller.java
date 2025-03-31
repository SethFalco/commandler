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

package fun.falco.commandler.annotation.stereotypes;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Stereotype;

import fun.falco.commandler.annotation.Command;

/**
 * Subset of {@link Command commands} and how all commands must be registered.
 *
 * <p>A {@link Controller} can be thought of as a module of commands. A
 * {@link Command} can be thought of as a single command.</p>
 *
 * @author seth@falco.fun (Seth Falco)
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@ApplicationScoped
@Stereotype
public @interface Controller {

    /**
     * @return
     *     If true the module will be hidden from help commands and
     *     documentation.
     */
    boolean hidden() default false;

    /**
     * @return Related {@link Controller}s that users may be interested in.
     */
    Class<?>[] seeAlso() default {};
}

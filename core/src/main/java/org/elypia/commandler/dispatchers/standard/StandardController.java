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

package org.elypia.commandler.dispatchers.standard;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Stereotype;

import org.elypia.commandler.annotation.AnnotationUtils;
import org.elypia.commandler.annotation.Property;
import org.elypia.commandler.annotation.PropertyWrapper;
import org.elypia.commandler.annotation.stereotypes.Controller;

/**
 * Denotes that this is a module or command that can be accessed
 * under aliases.
 *
 * @author seth@falco.fun (Seth Falco)
 */
@ApplicationScoped
@Stereotype
@Controller
@PropertyWrapper(type = StandardDispatcher.class)
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface StandardController {

    /**
     * @return The prefixes for all {@link StandardCommand}s in the {@link Controller}.
     */
    @Property(key = "aliases", i18n = true, isPublic = true, displayName = "Aliases")
    String value() default AnnotationUtils.EFFECTIVELY_NULL;

    /**
     * If this {@link Controller} is marked as static then
     * all {@link StandardCommand} will be static commands regardless
     * of the value of {@link StandardCommand#isStatic()}.
     *
     * @return If this command is static.
     */
    @Property(key = "static")
    boolean isStatic() default false;
}

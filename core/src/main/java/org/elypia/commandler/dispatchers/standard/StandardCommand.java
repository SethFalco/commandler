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

package org.elypia.commandler.dispatchers.standard;

import org.elypia.commandler.annotation.*;
import org.elypia.commandler.annotation.stereotypes.Controller;

import java.lang.annotation.*;

/**
 * Denotes that this is a module or command that can be accessed
 * under aliases.
 *
 * @author seth@elypia.org (Seth Falco)
 */
@Command
@PropertyWrapper(type = StandardDispatcher.class)
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface StandardCommand {

    /**
     * The aliases for this command. This is how the command can be performed.
     *
     * If the {@link StandardController} has an alias of <code>utils</code>, and
     * the {@link StandardCommand} has an alias of <code>ping</code>", then the
     * command will be <code>utils ping</code>.
     *
     * If {@link #isStatic()} is true for either the Controller or Command,
     * then just <code>ping</code> will work on it's own as well.
     *
     * @return The aliases for a command to execute this command.
     */
    @Property(key = "aliases", i18n = true, isPublic = true, displayName = "Aliases")
    String value() default AnnotationUtils.EFFECTIVELY_NULL;

    /**
     * If this command is marked as static,
     * it will be capable of being performed without the usage
     * of the parent {@link Controller}s alias.
     *
     * This is ignored if the {@link Controller} has the
     * {@link StandardController} annotation and specified
     * isStatic as true.
     *
     * @return If this command is static.
     */
    @Property(key = "static", value = "{#CLASS-org.elypia.commandler.dispatchers.static}")
    boolean isStatic() default false;

    /**
     * Each module can be assigned a default command. The default commands is the
     * default method we assume the user has intended when they are
     * attempting a command. The default will be called if either no
     * command is specified or the "command" specified isn't a valid
     * commands in the module. (Potentially a parameter.)
     *
     * @return If this command should be performed by default if no
     * other candidate matches.
     */
    @Property(key = "default")
    boolean isDefault() default false;
}

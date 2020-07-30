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

import org.elypia.commandler.annotation.stereotypes.Controller;
import org.elypia.commandler.groups.*;

import java.lang.annotation.*;

/**
 * Marks an annotation as a grouping annotation.
 * Grouping annotations can be applied to {@link Controller}s to
 * group them up in a type-safe manner.
 *
 * This makes is safer to put objects into groups, and makes it much
 * easier to programmatically use and sort through groups, or manage
 * internalization.
 *
 * @author seth@elypia.org (Seth Falco)
 * @since 4.1.0
 */
@Target({ElementType.ANNOTATION_TYPE, ElementType.TYPE, ElementType.PACKAGE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Group {

    /**
     * The key to use to obtain the localized name of this group.
     * It's recommended to leave this as null unless you're requirements
     * disallow you to.
     *
     * Leaving this as null defaults the key to be fully qualified
     * class name of the group annotation.
     *
     * <strong>
     *     This must be specified if it's applied on a {@link Controller} directly.
     *     It's strongly recommended to use this to create a grouping annotation
     *     such as the default {@link Guidance} and {@link Miscellaneous} annotations provided.
     *     Using the group annotation on types or packages directly is only for if
     *     your requirements absolutely disallow using it otherwise.
     * </strong>
     *
     * @return The message key that refers to the name of this group.
     */
    String message() default AnnotationUtils.EFFECTIVELY_NULL;
}

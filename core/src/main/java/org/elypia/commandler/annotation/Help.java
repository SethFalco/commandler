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

import java.lang.annotation.*;

/**
 * @author seth@elypia.org (Seth Falco)
 */
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Help {

    /**
     * @return The display name of this item.
     */
    String name() default AnnotationUtils.EFFECTIVELY_NULL;

    /**
     * @return A small description of what this does or contains.
     */
    String description() default AnnotationUtils.EFFECTIVELY_NULL;
}

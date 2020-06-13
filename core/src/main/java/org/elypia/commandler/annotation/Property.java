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
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Property {

    /**
     * @return The name of the property this is setting.
     */
    String key();

    /**
     * @return The key to set this property to, this should be
     * {@link AnnotationUtils#EFFECTIVELY_NULL} if this is for a Annotation wrapper
     * for a property.
     */
    String value() default AnnotationUtils.EFFECTIVELY_NULL;

    /**
     * @return If this value is allowed to be overridden by the
     * internationalization provider.
     */
    boolean i18n() default false;

    /**
     * @return If this should be displayed alongside other metadata,
     * public to users.
     */
    boolean isPublic() default false;

    /**
     * This is useful for properties which should be displayed to users
     * when the objects metadata is shown.
     * By default it's only useful when {@link #isPublic()}} is true.
     *
     * @return Returns the display name of this property for users.
     */
    String displayName() default AnnotationUtils.EFFECTIVELY_NULL;
}

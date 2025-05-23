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

package fun.falco.commandler.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Command annotation is used to supply metadata to commands. This can be
 * aliases, or the help to let people know how to use this commands.
 *
 * <p>All static data will be stored in an annotation, reserving the method body
 * for what it's meant for, logic.</p>
 *
 * @author seth@falco.fun (Seth Falco)
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Param {

    /**
     * @return
     *     Default value of this parameter if there is one, else it's mandatory.
     */
    String value() default AnnotationUtils.EFFECTIVELY_NULL;

    /**
     * This is especially useful when the default value is handled via Java
     * Expression Language.
     *
     * @return User friendly name for this display value.
     */
    String displayAs() default AnnotationUtils.EFFECTIVELY_NULL;
}

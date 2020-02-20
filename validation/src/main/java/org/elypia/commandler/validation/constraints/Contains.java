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

package org.elypia.commandler.validation.constraints;

import javax.validation.*;
import java.lang.annotation.*;
import java.util.Objects;

/**
 * @author seth@elypia.org (Seth Falco)
 */
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = Contains.Validator.class)
public @interface Contains {

    String message() default "{org.elypia.commandler.validation.constraints.Contains.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    String value();

    class Validator implements ConstraintValidator<Contains, String> {

        private String value;

        @Override
        public void initialize(Contains constraintAnnotation) {
            this.value = Objects.requireNonNull(constraintAnnotation.value());
        }

        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            if (value == null)
                return false;

            return value.contains(this.value);
        }
    }
}

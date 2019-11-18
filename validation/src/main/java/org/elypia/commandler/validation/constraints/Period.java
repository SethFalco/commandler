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

package org.elypia.commandler.validation.constraints;

import javax.validation.*;
import java.lang.annotation.*;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * @author seth@elypia.org (Seth Falco)
 */
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = Period.Validator.class)
public @interface Period {

    String message() default "{org.elypia.commandler.validation.constraints.Period.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    long min() default 0;

    long max() default Integer.MAX_VALUE;

    /**
     * This is internally validated using {@link TimeUnit#SECONDS}
     * so setting this to {@link TimeUnit#NANOSECONDS} to specify
     * a time under a second shouldn't do anything.
     */
    TimeUnit unit() default TimeUnit.SECONDS;

    class Validator implements ConstraintValidator<Period, Duration> {

        private static final TimeUnit SECONDS = TimeUnit.SECONDS;

        private long min;
        private long max;

        @Override
        public void initialize(Period constraintAnnotation) {
            TimeUnit unit = constraintAnnotation.unit();

            min = SECONDS.convert(constraintAnnotation.min(), unit);
            max = SECONDS.convert(constraintAnnotation.max(), unit);
        }

        @Override
        public boolean isValid(Duration value, ConstraintValidatorContext context) {
            long seconds = value.toSeconds();
            return (min <= seconds && seconds <= max);
        }
    }
}

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

import org.elypia.commandler.validation.validators.PeriodValidator;

import javax.validation.*;
import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * @author seth@elypia.org (Seth Falco)
 */
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = PeriodValidator.class)
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
}

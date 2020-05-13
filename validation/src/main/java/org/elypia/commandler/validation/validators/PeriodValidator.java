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

package org.elypia.commandler.validation.validators;

import org.elypia.commandler.validation.constraints.Period;

import javax.validation.*;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class PeriodValidator implements ConstraintValidator<Period, Duration> {

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

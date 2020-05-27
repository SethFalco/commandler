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

package org.elypia.commandler.validation.validators;

import org.elypia.commandler.validation.constraints.Option;

import javax.validation.*;

public class OptionValidator implements ConstraintValidator<Option, String> {

    private String[] value;

    @Override
    public void initialize(Option constraintAnnotation) {
        value = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        for (String option : this.value) {
            if (option.equals(value))
                return true;
        }

        return false;
    }
}

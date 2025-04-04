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

package fun.falco.commandler.validation.validators;

import java.util.Objects;

import javax.enterprise.context.ApplicationScoped;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import fun.falco.commandler.validation.constraints.Contains;

@ApplicationScoped
public class ContainsValidator implements ConstraintValidator<Contains, String> {

    private String value;

    @Override
    public void initialize(Contains constraintAnnotation) {
        this.value = Objects.requireNonNull(constraintAnnotation.value());
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }

        return value.contains(this.value);
    }
}

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

import org.elypia.commandler.api.Integration;
import org.elypia.commandler.event.ActionEvent;
import org.elypia.commandler.validation.constraints.Platform;

import javax.enterprise.context.ApplicationScoped;
import javax.validation.*;

@ApplicationScoped
public class PlatformValidator implements ConstraintValidator<Platform, ActionEvent<?, ?>> {

    private Class<? extends Integration>[] controllerTypes;

    @Override
    public void initialize(Platform constraintAnnotation) {
        this.controllerTypes = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(ActionEvent<?, ?> event, ConstraintValidatorContext context) {
        Integration<?, ?> controller = event.getRequest().getIntegration();

        for (Class<? extends Integration> type : controllerTypes) {
            if (type == controller.getClass())
                return true;
        }

        return false;
    }
}

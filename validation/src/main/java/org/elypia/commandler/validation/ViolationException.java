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

package org.elypia.commandler.validation;

import org.elypia.commandler.api.Controller;
import org.elypia.commandler.event.ActionEvent;
import org.elypia.commandler.exceptions.misuse.ActionException;

import javax.validation.ConstraintViolation;
import java.util.*;

/**
 * @author seth@elypia.org (Seth Falco)
 */
public class ViolationException extends ActionException {

    private final transient Set<ConstraintViolation<Controller>> violations;

    public ViolationException(ActionEvent<?, ?> action, Set<ConstraintViolation<Controller>> violations) {
        super(action);
        this.violations = Objects.requireNonNull(violations);
    }

    public Set<ConstraintViolation<Controller>> getViolations() {
        return violations;
    }
}

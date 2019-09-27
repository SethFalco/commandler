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

package org.elypia.commandler.exceptions;

import org.elypia.commandler.event.ActionEvent;

import java.util.Objects;

/**
 * @author seth@elypia.org (Syed Shah)
 */
public abstract class ActionException extends RuntimeException {

    private ActionEvent<?, ?> event;

    public ActionException(ActionEvent<?, ?> event) {
        super();
        this.event = Objects.requireNonNull(event);
    }

    public ActionException(ActionEvent<?, ?> event, String message) {
        super(message);
        this.event = Objects.requireNonNull(event);
    }

    public ActionException(ActionEvent<?, ?> event, String message, Throwable cause) {
        super(message, cause);
        this.event = Objects.requireNonNull(event);
    }

    public ActionException(ActionEvent<?, ?> event, Throwable cause) {
        super(cause);
        this.event = Objects.requireNonNull(event);
    }

    public ActionEvent<?, ?> getActionEvent() {
        return event;
    }
}

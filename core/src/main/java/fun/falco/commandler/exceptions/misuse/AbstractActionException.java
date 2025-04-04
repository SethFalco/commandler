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

package fun.falco.commandler.exceptions.misuse;

import java.util.Objects;

import org.apache.deltaspike.core.api.exception.control.ExceptionHandler;

import fun.falco.commandler.event.ActionEvent;

/**
 * All exceptions that should panic and stop processing a command
 * due to user error should extend this abstract class.
 *
 * <p>To be thrown when users have done something wrong in order for an
 * {@link ExceptionHandler} to pick up the error and return a clean and helpful
 * error message to users.</p>
 *
 * @author seth@falco.fun (Seth Falco)
 */
public abstract class AbstractActionException extends AbstractMisuseException {

    private final transient ActionEvent<?, ?> event;

    public AbstractActionException(ActionEvent<?, ?> event) {
        super();
        this.event = Objects.requireNonNull(event);
    }

    public AbstractActionException(ActionEvent<?, ?> event, String message) {
        super(message);
        this.event = Objects.requireNonNull(event);
    }

    public AbstractActionException(ActionEvent<?, ?> event, String message, Throwable cause) {
        super(message, cause);
        this.event = Objects.requireNonNull(event);
    }

    public AbstractActionException(ActionEvent<?, ?> event, Throwable cause) {
        super(cause);
        this.event = Objects.requireNonNull(event);
    }

    public ActionEvent getActionEvent() {
        return event;
    }
}

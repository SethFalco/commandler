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

package org.elypia.commandler.exceptions.misuse;

import org.apache.deltaspike.core.api.exception.control.ExceptionHandler;

/**
 * All exceptions that should panic and stop processing a command
 * due to user error should extend this abstract class.
 *
 * The {@link AbstractMisuseException} is intended to be thrown when
 * users have done something wrong in order for an {@link ExceptionHandler}
 * to pick up the error and return a clean and helpful error
 * message to users.
 *
 * @author seth@elypia.org (Seth Falco)
 */
public abstract class AbstractMisuseException extends RuntimeException {

    public AbstractMisuseException() {
        super();
    }

    public AbstractMisuseException(String message) {
        super(message);
    }

    public AbstractMisuseException(String message, Throwable cause) {
        super(message, cause);
    }

    public AbstractMisuseException(Throwable cause) {
        super(cause);
    }
}

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

package org.elypia.commandler.exceptions.misuse;

import org.elypia.commandler.api.MisuseHandler;

/**
 * All exceptions that should panic and stop processing a command
 * due to user error should extend this abstract class.
 *
 * The {@link MisuseException} is intended to be thrown when
 * users have done something wrong in order for a {@link MisuseHandler}
 * to pick up the error and return a clean and helpful error
 * message to users.
 *
 * @author seth@elypia.org (Seth Falco)
 */
public abstract class MisuseException extends RuntimeException {

    public MisuseException() {
        super();
    }

    public MisuseException(String message) {
        super(message);
    }

    public MisuseException(String message, Throwable cause) {
        super(message, cause);
    }

    public MisuseException(Throwable cause) {
        super(cause);
    }
}

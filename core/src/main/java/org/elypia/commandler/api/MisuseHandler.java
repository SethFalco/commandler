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

package org.elypia.commandler.api;

import org.elypia.commandler.exceptions.misuse.MisuseException;

/**
 * This could handle a single exception, or route the
 * exeception to a method to handle that particular exception.
 *
 * @author seth@elypia.org (Seth Falco)
 */
@FunctionalInterface
public interface MisuseHandler {

    /**
     * Handle the exception that occured if relevent
     * for this {@link MisuseHandler} else throw nothing and
     * return to move to the next handler.
     *
     * @param ex The exception that occured.
     * @param <X> The type of exception that occured.
     * @return The object or message to send due to this exception.
     */
    <X extends MisuseException> Object handle(X ex);
}

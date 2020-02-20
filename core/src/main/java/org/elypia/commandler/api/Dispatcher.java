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

import org.elypia.commandler.Request;
import org.elypia.commandler.event.ActionEvent;

/**
 * The {@link Dispatcher} has the role of processing an event
 * from your respective platform into something that can be interperted
 * by Commandler, this should be used to verify if it's a command as well as
 * parse it into an input and event object to be used internally.
 *
 * @author seth@elypia.org (Seth Falco)
 */
public interface Dispatcher {

    /**
     * @param request The action request made by the {@link Integration}.
     * @param <S>
     * @param <M>
     * @return If this is a valid command or not.
     * This does not mean it is a command, it's just for checking if the text
     * is formatted in a way that it fits the formatting of a command.
     */
    <S, M> boolean isValid(Request<S, M> request);

    /**
     * Break the command down into it's individual components.
     *
     * @param request The action request made by the {@link Integration}.
     * @param <S>
     * @param <M>
     * @return The input the user provided or null if it's not a valid command.
     */
    <S, M> ActionEvent<S, M> parse(Request<S, M> request);
}

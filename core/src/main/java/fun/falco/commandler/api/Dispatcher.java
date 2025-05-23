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

package fun.falco.commandler.api;

import fun.falco.commandler.event.ActionEvent;
import fun.falco.commandler.event.Request;

/**
 * The {@link Dispatcher} has the role of processing an event from your
 * respective platform into something that can be interpreted by Commandler,
 * this should be used to verify if it's a command as well as parse it into an
 * input and event object to be used internally.
 *
 * @author seth@falco.fun (Seth Falco)
 */
public interface Dispatcher {

    /**
     * @param request Action request made by the {@link Integration}.
     * @param <S> Type of source event this {@link Integration} is for.
     * @param <M> Type of message this {@link Integration} sends and received.
     * @return
     *     If this is a valid command or not. This does not mean it is a
     *     command, it's just for checking if the text is formatted in a way
     *     that it fits the formatting of a command.
     */
    <S, M> boolean isValid(Request<S, M> request);

    /**
     * Break the command down into it's individual components.
     *
     * @param request Action request made by the {@link Integration}.
     * @param <S> Type of source even thtis {@link Integration} is for.
     * @param <M> Type of message this {@link Integration} sends and received.
     * @return Input the user provided or null if it's not a valid command.
     */
    <S, M> ActionEvent<S, M> parse(Request<S, M> request);
}

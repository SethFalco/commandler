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

import fun.falco.commandler.ActionHandler;
import fun.falco.commandler.Commandler;

/**
 * When Commandler receives an Action from any of the registered
 * {@link Integration}s, it goes through all registered {@link ActionListener}.
 *
 * <p>One {@link ActionListener} implementation is provided by default which is
 * the {@link ActionHandler}.</p>
 *
 * @author seth@falco.fun (Seth Falco)
 */
public interface ActionListener {

    /**
     * @param integration Integration that handed {@link Commandler} this event.
     * @param source Original event that the {@link Integration} received.
     * @param message Message object provided by the {@link Integration}.
     * @param content Raw content of the message as perceived by the integration.
     * @param <S> Type of source event this {@link Integration} is for.
     * @param <M> Type of message this {@link Integration} sends and received.
     * @return Response to this action, or null if no response was given.
     */
    <S, M> M onAction(Integration<S, M> integration, S source, M message, String content);
}

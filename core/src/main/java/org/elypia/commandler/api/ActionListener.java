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

import org.elypia.commandler.*;

/**
 * Whenever Commandler recieves an Action from any of the registered
 * {@link Integration}s, it goes through all registred {@link ActionListener}.
 *
 * One {@link ActionListener} implementation is provided by default which
 * is the {@link ActionHandler}.
 *
 * @author seth@elypia.org (Seth Falco)
 */
public interface ActionListener {

    /**
     * @param integration The integration that handed {@link Commandler} this event.
     * @param source The original event that the {@link Integration} received.
     * @param message The message object provided by the {@link Integration}.
     * @param content The raw content of the message as percieved by the integration.
     * @param <S> The type of source even thtis {@link Integration} is for.
     * @param <M> The type of message this {@link Integration} sends and received.
     * @return The response to this action, or null if no response was given.
     */
    <S, M> M onAction(Integration<S, M> integration, S source, M message, String content);
}

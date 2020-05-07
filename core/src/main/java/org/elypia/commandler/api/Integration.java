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

import java.io.Serializable;

/**
 * An integration represents an class which manages the integration
 * or implementation of Commandler with another service or platform.
 *
 * This could represent a terminal if it's a console application
 * or a form of social media such as Discord.
 *
 * @param <S> The type of source event this integration consumes. If there
 * are multiple types, specify the highest level expected to be used.
 * @param <M> The type of message this platform sends and receives.
 * @author seth@elypia.org (Seth Falco)
 */
public interface Integration<S, M> {

    /**
     * @return The type of object that is sent and received by this integration.
     */
    Class<M> getMessageType();

    /**
     * @param source The event this integration has received.
     * @return A unique and {@link Serializable} ID that represents this action.
     * This can also be a generated ID if the integration does not provide
     * unique IDs itself.
     */
    Serializable getActionId(S source);
}

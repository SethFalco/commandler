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

import org.elypia.commandler.ActionHandler;
import org.elypia.commandler.event.ActionEvent;

/**
 * This can be implemented to add additional steps
 * during action handling, these steps will typically
 * only manipulate parameters or perform pre or post
 * handling steps.
 *
 * If you want to make drastic changes to how action handling
 * is performed, you may want to implement {@link ActionHandler} instead.
 *
 * @author seth@elypia.org (Seth Falco)
 */
public interface HandlerMiddleware {

    /**
     * @param event The event that represents the current request.
     * @param <S> The type of source even thtis {@link Integration} is for.
     * @param <M> The type of message this {@link Integration} sends and received.
     */
    <S, M> void onMiddleware(ActionEvent<S, M> event);
}

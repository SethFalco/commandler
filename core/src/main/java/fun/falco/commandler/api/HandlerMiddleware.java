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
import fun.falco.commandler.event.ActionEvent;

/**
 * This can be implemented to add additional steps during action handling, these
 * steps will typically only manipulate parameters or perform pre or post
 * handling steps.
 *
 * <p>If you want to make drastic changes to how action handling is performed,
 * you may want to implement {@link ActionHandler} instead.</p>
 *
 * @author seth@falco.fun (Seth Falco)
 */
public interface HandlerMiddleware {

    /**
     * @param event Event that represents the current request.
     * @param <S> Type of source event this {@link Integration} is for.
     * @param <M> Type of message this {@link Integration} sends and received.
     */
    <S, M> void onMiddleware(ActionEvent<S, M> event);
}

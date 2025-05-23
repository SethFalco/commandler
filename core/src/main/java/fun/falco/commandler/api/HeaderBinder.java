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

import java.util.Map;

import fun.falco.commandler.dispatchers.standard.StandardDispatcher;
import fun.falco.commandler.event.Request;

/**
 * Implement this to attach metadata for requests. Headers are used to manage
 * data for requests which could be defined here either because it's dynamic
 * data, or obtained from external sources, but relevant to the processing of a
 * command.
 *
 * <p>An example of this could be if your requirements dictate the
 * {@link StandardDispatcher} must operate with a different prefix depending on
 * the user performing the command.</p>
 *
 * <p>The {@link StandardDispatcher} could be configured to use ${USER_PREFIX} and
 * a {@link HeaderBinder} could be used to set the header
 * <code>USER_PREFIX</code> per event.</p>
 *
 * @author seth@falco.fun (Seth Falco)
 */
@FunctionalInterface
public interface HeaderBinder {

    /**
     * @param request Serializable request of made by the user.
     * @param <S> Type of source event this {@link Integration} is for.
     * @param <M> Type of message this {@link Integration} sends and received.
     * @return Headers to add to the request, or null if none.
     */
    <S, M> Map<String, String> bind(Request<S, M> request);
}

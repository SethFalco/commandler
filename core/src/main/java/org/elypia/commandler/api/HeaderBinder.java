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

package org.elypia.commandler.api;

import org.elypia.commandler.Request;
import org.elypia.commandler.dispatchers.StandardDispatcher;

import java.util.Map;

/**
 * Implement this to attach metadata for requests.
 * Headers are used to manage data for requests which could
 * be defined here either because it's dynamic data, or obtained
 * from external sources, but relevent to the processing of a command.
 *
 * An example of this could be if your requirements dictate
 * the {@link StandardDispatcher} must operate with a different prefix
 * depending on the user performing the command.
 *
 * The {@link StandardDispatcher} could be configured to use ${USER_PREFIX}
 * and a {@link HeaderBinder} could be used to set the header
 * <code>USER_PREFIX</code> per event.
 *
 * @author seth@elypia.org (Seth Falco)
 */
@FunctionalInterface
public interface HeaderBinder {

    /**
     * @param request
     * @param <S>
     * @param <M>
     * @return
     */
    <S, M> Map<String, String>  bind(Request<S, M> request);
}

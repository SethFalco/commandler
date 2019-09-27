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

package org.elypia.commandler.console.messengers;

import org.elypia.commandler.api.ResponseBuilder;
import org.elypia.commandler.event.ActionEvent;

import javax.inject.Singleton;

/**
 * A provider for types that can make desireable output from
 * just the {@link Object#toString()} method.
 *
 * @author seth@elypia.org (Syed Shah)
 */
@Singleton
public class MiscMessenger implements ResponseBuilder<Object, String> {

    @Override
    public String provide(ActionEvent<?, String> event, Object output) {
        return output.toString();
    }
}

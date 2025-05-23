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

package fun.falco.commandler.console.messengers;

import java.net.URL;

import fun.falco.commandler.annotation.stereotypes.MessageProvider;
import fun.falco.commandler.api.Messenger;
import fun.falco.commandler.event.ActionEvent;

/**
 * Provider for types that can make desirable output from just the
 * {@link Object#toString()} method.
 *
 * @author seth@falco.fun (Seth Falco)
 */
@MessageProvider(provides = String.class, value = {Boolean.class, CharSequence.class, Character.class, URL.class})
public class MiscMessenger implements Messenger<Object, String> {

    @Override
    public String provide(ActionEvent<?, String> event, Object output) {
        return output.toString();
    }
}

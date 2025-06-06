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

package fun.falco.commandler.adapters;

import fun.falco.commandler.annotation.stereotypes.ParamAdapter;
import fun.falco.commandler.api.Adapter;
import fun.falco.commandler.event.ActionEvent;
import fun.falco.commandler.metadata.MetaParam;

/**
 * @author seth@falco.fun (Seth Falco)
 */
@ParamAdapter(Character.class)
public class CharAdapter implements Adapter<Character> {

    @Override
    public Character adapt(String input, Class<? extends Character> type, MetaParam data, ActionEvent<?, ?> event) {
        if (input.length() == 1) {
            return input.charAt(0);
        }

        if (input.length() < 4) {
            try {
                return (char) Byte.parseByte(input);
            } catch (NumberFormatException ex) {
                return null;
            }
        }

        return null;
    }
}

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

package org.elypia.commandler.adapters;

import org.elypia.commandler.api.Adapter;
import org.elypia.commandler.event.ActionEvent;
import org.elypia.commandler.metadata.MetaParam;

import javax.inject.Singleton;

/**
 * @author seth@elypia.org (Seth Falco)
 */
@Singleton
public class CharAdapter implements Adapter<Character> {

    @Override
    public Character adapt(String input, Class<? extends Character> type, MetaParam data, ActionEvent<?, ?> event) {
        if (input.length() == 1)
            return input.charAt(0);

        if (input.length() < 4) {
            try {
                return (char)Byte.parseByte(input);
            } catch (NumberFormatException ex) {
                return null;
            }
        }

        return null;
    }
}

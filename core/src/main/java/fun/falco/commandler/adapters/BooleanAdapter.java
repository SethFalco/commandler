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

import java.util.Collection;
import java.util.List;

import fun.falco.commandler.annotation.stereotypes.ParamAdapter;
import fun.falco.commandler.api.Adapter;
import fun.falco.commandler.event.ActionEvent;
import fun.falco.commandler.metadata.MetaParam;

/**
 * @author seth@falco.fun (Seth Falco)
 */
@ParamAdapter(Boolean.class)
public class BooleanAdapter implements Adapter<Boolean> {

    private static final Collection<String> TRUE = List.of(
        "true", "t", // Formal responses
        "1", "one", // Developer responses
        "yes", "y", // Normal responses
        "ya", "ye", "yea", "yeah", // Informal responses
        "✔" // Characters
    );

    private static final Collection<String> FALSE = List.of(
        "false", "f", // Formal responses
        "0", "zero", // Developer responses
        "no", "n", // Normal responses
        "nah", "nope", // Informal responses
        "❌" // Characters
    );

    @Override
    public Boolean adapt(String input, Class<? extends Boolean> type, MetaParam data, ActionEvent<?, ?> event) {
        input = input.toLowerCase();

        if (TRUE.contains(input)) {
            return true;
        }

        if (FALSE.contains(input)) {
            return false;
        }

        return null;
    }
}

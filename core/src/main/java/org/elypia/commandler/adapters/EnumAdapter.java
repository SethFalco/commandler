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

package org.elypia.commandler.adapters;

import org.elypia.commandler.Commandler;
import org.elypia.commandler.annotation.StringValues;
import org.elypia.commandler.annotation.stereotypes.ParamAdapter;
import org.elypia.commandler.api.Adapter;
import org.elypia.commandler.event.ActionEvent;
import org.elypia.commandler.metadata.MetaParam;
import org.slf4j.*;

import java.util.Objects;

/**
 * This is the generic {@link Enum} parser. This should be the fallback parser
 * for any enums that are registered to {@link Commandler} if a
 * {@link Adapter} of that type was not specifically registered. <br>
 * This simply checks if the names are the same after removing
 * spaces, under scores, and converting to lower case.
 *
 * @author seth@elypia.org (Seth Falco)
 */
@ParamAdapter(Enum.class)
public class EnumAdapter implements Adapter<Enum<?>> {

    private static final Logger logger = LoggerFactory.getLogger(EnumAdapter.class);

    @Override
    public Enum<?> adapt(String input, Class<? extends Enum<?>> type, MetaParam data, ActionEvent<?, ?> event) {
        Objects.requireNonNull(type);
        Objects.requireNonNull(input);
        input = input.toLowerCase().replace(" ", "");

        for (Enum<?> e : type.getEnumConstants()) {
            String name = e.name().toLowerCase().replace("_", "");

            if (name.equals(input))
                return e;

            if (checkStringValues(input, e, type))
                return e;
        }

        return null;
    }

    private boolean checkStringValues(String input, Enum<?> e, Class<? extends Enum<?>> type) {
        StringValues values = null;

        try {
            values = type.getField(e.name()).getAnnotation(StringValues.class);
        } catch (NoSuchFieldException ex) {
            logger.error("This shouldn't be possible.", ex);
        }

        if (values == null)
            return false;

        for (String string : values.value()) {
            if (values.isCaseSensitive()) {
                if (string.equals(input))
                    return true;
            } else if (string.equalsIgnoreCase(input)) {
                return true;
            }
        }

        return false;
    }
}

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

import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Objects;

import javax.inject.Inject;

import fun.falco.commandler.annotation.stereotypes.ParamAdapter;
import fun.falco.commandler.api.Adapter;
import fun.falco.commandler.event.ActionEvent;
import fun.falco.commandler.metadata.MetaParam;

/**
 * @author seth@falco.fun (Seth Falco)
 */
@ParamAdapter({Long.class, Integer.class, Short.class, Byte.class, Double.class, Float.class})
public class NumberAdapter implements Adapter<Number> {

    private NumberFormat defaultFormat;

    public NumberAdapter() {
        this(NumberFormat.getInstance());
    }

    @Inject
    public NumberAdapter(NumberFormat defaultFormat) {
        this.defaultFormat = defaultFormat;
    }

    @Override
    public Number adapt(String input, Class<? extends Number> type, MetaParam metaParam, ActionEvent<?, ?> event) {
        Objects.requireNonNull(input);
        Objects.requireNonNull(type);

        ParsePosition position = new ParsePosition(0);
        Number number = defaultFormat.parse(input, position);

        if (position.getErrorIndex() != -1 || input.length() != position.getIndex()) {
            return null;
        }

        if (type == Double.class || type == double.class) {
            return number.doubleValue();
        }
        if (type == Float.class || type == float.class) {
            return number.floatValue();
        }

        if (type == Long.class || type == long.class) {
            return number.longValue();
        }
        if (type == Integer.class || type == int.class) {
            return number.intValue();
        }
        if (type == Short.class || type == short.class) {
            return number.shortValue();
        }
        if (type == Byte.class || type == byte.class) {
            return number.byteValue();
        }

        throw new IllegalStateException(NumberAdapter.class + " does not support the type " + type + ".");
    }

    /**
     * Calls {@link #adapt(String, Class, MetaParam)} but uses the default type
     * of {@link Integer}.
     *
     * @param input Parameter input.
     * @return Number or null if it wasn't possible to adapt this.
     * @see #adapt(String, Class, MetaParam)
     */
    @Override
    public Number adapt(String input) {
        return adapt(input, Integer.class);
    }
}

package com.elypia.commandler.adapters;

import com.elypia.commandler.CommandlerEvent;
import com.elypia.commandler.annotations.Adapter;
import com.elypia.commandler.interfaces.ParamAdapter;
import com.elypia.commandler.metadata.MetaParam;

import javax.inject.Inject;
import java.text.*;
import java.util.Objects;

@Adapter({Number.class, double.class, float.class, long.class, int.class, short.class, byte.class})
public class NumberAdapter implements ParamAdapter<Number> {

    private NumberFormat defaultFormat;

    public NumberAdapter() {
        this(NumberFormat.getInstance());
    }

    @Inject
    public NumberAdapter(NumberFormat defaultFormat) {
        this.defaultFormat = defaultFormat;
    }

    @Override
    public Number adapt(String input, Class<? extends Number> type, MetaParam param, CommandlerEvent<?> event) {
        Objects.requireNonNull(input);
        Objects.requireNonNull(type);

        ParsePosition position = new ParsePosition(0);
        Number number = defaultFormat.parse(input, position);

        if (position.getErrorIndex() != -1 || input.length() != position.getIndex())
            return null;

        if (type == Double.class || type == double.class)
            return number.doubleValue();
        if (type == Float.class || type == float.class)
            return number.floatValue();

        if (type == Long.class || type == long.class)
            return number.longValue();
        if (type == Integer.class || type == int.class)
            return number.intValue();
        if (type == Short.class || type == short.class)
            return number.shortValue();
        if (type == Byte.class || type == byte.class)
            return number.byteValue();

        return null;
    }

    /**
     * Calls {@link #adapt(String, Class, MetaParam)} but uses
     * the default type of {@link Integer}.
     *
     * @see #adapt(String, Class, MetaParam)
     * @param input The parameter input.
     * @return The number or null if it wasn't possible to adapt this.
     */
    @Override
    public Number adapt(String input) {
        return adapt(input, Integer.class);
    }
}

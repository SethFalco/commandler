package com.elypia.commandler.adapters;

import com.elypia.commandler.annotations.Compatible;
import com.elypia.commandler.interfaces.Adapter;
import com.elypia.commandler.metadata.data.MetaParam;

import javax.inject.*;
import java.text.*;
import java.util.Objects;

@Singleton
@Compatible({Number.class, double.class, float.class, long.class, int.class, short.class, byte.class})
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
    public Number adapt(String input, Class<? extends Number> type, MetaParam data) {
        Objects.requireNonNull(input);
        Objects.requireNonNull(type);

        try {
            Number number = defaultFormat.parse(input);

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
        } catch (ParseException e) {
            return null;
        }

        return null;
    }
}

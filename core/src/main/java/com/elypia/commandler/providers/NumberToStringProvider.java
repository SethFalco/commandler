package com.elypia.commandler.providers;

import com.elypia.commandler.annotations.Provider;
import com.elypia.commandler.interfaces.ResponseProvider;

import javax.inject.Inject;
import java.text.NumberFormat;

@Provider(provides = String.class, value = {Number.class, double.class, float.class, long.class, int.class, short.class, byte.class})
public class NumberToStringProvider implements ResponseProvider<Number, String> {

    private NumberFormat format;

    public NumberToStringProvider() {
        this(NumberFormat.getInstance());
    }

    @Inject
    public NumberToStringProvider(NumberFormat format) {
        this.format = format;
    }

    @Override
    public String provide(Number output) {
        return format.format(output);
    }
}

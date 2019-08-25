package com.elypia.commandler.console.messengers;

import com.elypia.commandler.api.ResponseBuilder;
import com.elypia.commandler.event.ActionEvent;

import javax.inject.Inject;
import java.text.NumberFormat;

public class NumberMessenger implements ResponseBuilder<Number, String> {

    private NumberFormat format;

    public NumberMessenger() {
        this(NumberFormat.getInstance());
    }

    @Inject
    public NumberMessenger(NumberFormat format) {
        this.format = format;
    }

    @Override
    public String provide(ActionEvent<?, String> event, Number output) {
        return format.format(output);
    }
}

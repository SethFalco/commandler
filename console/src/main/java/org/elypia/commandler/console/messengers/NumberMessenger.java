package org.elypia.commandler.console.messengers;

import org.elypia.commandler.api.ResponseBuilder;
import org.elypia.commandler.event.ActionEvent;

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

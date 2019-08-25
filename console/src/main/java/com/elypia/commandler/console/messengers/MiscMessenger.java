package com.elypia.commandler.console.messengers;

import com.elypia.commandler.api.ResponseBuilder;
import com.elypia.commandler.event.ActionEvent;

import javax.inject.Singleton;

/**
 * A provider for types that can make desireable output from
 * just the {@link Object#toString()} method.
 */
@Singleton
public class MiscMessenger implements ResponseBuilder<Object, String> {

    @Override
    public String provide(ActionEvent<?, String> event, Object output) {
        return output.toString();
    }
}

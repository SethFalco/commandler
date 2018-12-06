package com.elypia.commandler.console.builders;

import com.elypia.commandler.console.*;
import com.elypia.commandler.console.client.StringClient;

// ? This returns null on purpose to cause an error.
public class StringClientBuilder implements IStringBuilder<StringClient> {

    @Override
    public String build(StringCommand event, StringClient output) {
        return null;
    }
}

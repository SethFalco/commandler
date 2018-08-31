package com.elypia.commandler.string.builders;

import com.elypia.commandler.string.*;
import com.elypia.commandler.string.client.StringClient;

// ? This returns null on purpose to cause an error.
public class StringClientBuilder implements IStringBuilder<StringClient> {

    @Override
    public String build(StringCommand event, StringClient output) {
        return null;
    }
}

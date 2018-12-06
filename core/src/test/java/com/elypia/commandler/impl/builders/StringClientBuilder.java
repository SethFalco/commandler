package com.elypia.commandler.impl.builders;

import com.elypia.commandler.console.*;
import com.elypia.commandler.impl.*;
import com.elypia.commandler.test.impl.*;
import console.*;
import com.elypia.commandler.impl.client.StringClient;

public class StringClientBuilder implements IStringBuilder<StringClient> {

    @Override
    public String build(StringCommand event, StringClient output) {
        return null;
    }
}

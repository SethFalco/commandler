package com.elypia.commandler.test.integration.impl.builders;

import com.elypia.commandler.annotations.Compatible;
import com.elypia.commandler.test.integration.impl.TestProvider;

@Compatible(String.class)
public class DefaultProvider implements TestProvider<String> {

    @Override
    public String build(AbstractCommandlerEvent<String, String> event, String output) {
        return output;
    }
}

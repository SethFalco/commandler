package com.elypia.commandler.test.integration.impl.builders;

import com.elypia.commandler.interfaces.ResponseProvider;

public class DefaultResponseProvider implements ResponseProvider<AbstractCommandlerEvent<String, String>, String> {

    @Override
    public String build(AbstractCommandlerEvent<String, String> event, String output) {
        return output;
    }
}

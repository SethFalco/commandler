package com.elypia.commandler.test.integration.impl.builders;

import com.elypia.commandler.interfaces.ResponseProvider;

public class NumberResponseProvider implements ResponseProvider<AbstractCommandlerEvent<String, String>, Number> {

    @Override
    public String build(AbstractCommandlerEvent<String, String> event, Number output) {
        return String.valueOf(output);
    }
}

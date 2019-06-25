package com.elypia.commandler.test.integration.impl.builders;

import com.elypia.commandler.annotations.Provider;
import com.elypia.commandler.interfaces.ResponseProvider;

/** This adapters intentionally returns null which will make Commandler get mad. */
@Provider(provides = String.class, value = Object.class)
public class NullBuilder implements ResponseProvider<Object, String> {

    @Override
    public String provide(Object output) {
        return null;
    }
}

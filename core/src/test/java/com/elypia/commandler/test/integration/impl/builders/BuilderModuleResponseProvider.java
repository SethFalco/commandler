package com.elypia.commandler.test.integration.impl.builders;

import com.elypia.commandler.interfaces.ResponseProvider;
import com.elypia.commandler.test.integration.impl.modules.BuilderModule;

/**
 * This adapters intentionally returns null on load.
 */
public class BuilderModuleResponseProvider implements ResponseProvider<AbstractCommandlerEvent<String, String>, BuilderModule> {

    @Override
    public String build(AbstractCommandlerEvent<String, String> event, BuilderModule output) {
        return null;
    }
}

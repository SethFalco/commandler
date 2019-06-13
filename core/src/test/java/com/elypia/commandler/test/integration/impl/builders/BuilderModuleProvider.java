package com.elypia.commandler.test.integration.impl.builders;

import com.elypia.commandler.annotations.Compatible;
import com.elypia.commandler.test.integration.impl.TestProvider;
import com.elypia.commandler.test.integration.impl.modules.BuilderModule;

/**
 * This adapters intentionally returns null on load.
 */
@Compatible(BuilderModule.class)
public class BuilderModuleProvider implements TestProvider<BuilderModule> {

    @Override
    public String build(AbstractCommandlerEvent<String, String> event, BuilderModule output) {
        return null;
    }
}

package com.elypia.commandler.test.impl.builders;

import com.elypia.commandler.annotations.Compatible;
import com.elypia.commandler.impl.CommandEvent;
import com.elypia.commandler.test.impl.ITestBuilder;
import com.elypia.commandler.test.impl.modules.BuilderModule;

/**
 * This builder intentionally returns null on build.
 */
@Compatible(BuilderModule.class)
public class BuilderModuleBuilderI implements ITestBuilder<BuilderModule> {

    @Override
    public String build(CommandEvent<String, String> event, BuilderModule output) {
        return null;
    }
}

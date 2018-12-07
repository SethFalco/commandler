package com.elypia.commandler.impl.builders;

import com.elypia.commandler.impl.*;
import com.elypia.commandler.impl.modules.BuilderModule;

/**
 * This builder intentionally returns null on build.
 */
public class BuilderModuleBuilder implements TestBuilder<BuilderModule> {

    @Override
    public String build(TestEvent event, BuilderModule output) {
        return null;
    }
}

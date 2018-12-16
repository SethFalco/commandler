package com.elypia.commandler.impl.builders;

import com.elypia.commandler.annotations.Compatible;
import com.elypia.commandler.impl.*;
import com.elypia.commandler.impl.modules.BuilderModule;

/**
 * This builder intentionally returns null on build.
 */
@Compatible(BuilderModule.class)
public class BuilderModuleBuilder implements TestBuilder<BuilderModule> {

    @Override
    public String build(CommandEvent<String, String> event, BuilderModule output) {
        return null;
    }
}

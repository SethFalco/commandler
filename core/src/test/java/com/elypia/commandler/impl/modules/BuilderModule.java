package com.elypia.commandler.impl.modules;

import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.*;

@Module(name = "Builder", aliases = "builder", help = "Module for testing if builders are performing correctly.")
public class BuilderModule {

    @Command(name = "Builder Returned Null", aliases = "null", help = "When a builder returns null for input.")
    public BuilderModule builderReturnedNull() {
        return new BuilderModule();
    }
}

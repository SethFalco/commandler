package com.elypia.commandler.test.integration.impl.modules;

import com.elypia.commandler.Handler;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.test.integration.impl.builders.BuilderModuleProvider;

@Module(name = "Builders", aliases = "adapters", help = "Module for testing if builders are performing correctly.")
public class BuilderModule implements Handler {

    /**
     * @return Returns a new {@link BuilderModule} instance
     * this is good for this test as we have registered a adapters called
     * {@link BuilderModuleProvider} which will always return null upon building
     * this type of object. Builders should never return null!
     */
    @Command(name = "Builders Returned Null", aliases = "null", help = "When a adapters returns null for input.")
    public BuilderModule builderReturnedNull() {
        return new BuilderModule();
    }
}

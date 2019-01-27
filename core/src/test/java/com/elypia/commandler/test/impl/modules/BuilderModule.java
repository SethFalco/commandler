package com.elypia.commandler.test.impl.modules;

import com.elypia.commandler.Commandler;
import com.elypia.commandler.Handler;
import com.elypia.commandler.annotations.Command;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.metadata.ModuleData;
import com.elypia.commandler.test.impl.builders.BuilderModuleBuilderI;

@Module(id = "Builders", aliases = "builder", help = "Module for testing if builders are performing correctly.")
public class BuilderModule extends Handler<String, String> {

    /**
     * Initialise the module, this will assign the values
     * in the module and create a {@link ModuleData} which is
     * what {@link Commandler} uses in runtime to identify modules,
     * commands or obtain any static data.
     *
     * @param commandler Our parent Commandler class.
     */
    public BuilderModule(Commandler<String, String> commandler) {
        super(commandler);
    }

    /**
     * @return Returns a new {@link BuilderModule} instance
     * this is good for this test as we have registered a builder called
     * {@link BuilderModuleBuilderI} which will always return null upon building
     * this type of object. Builders should never return null!
     */
    @Command(id = "Builders Returned Null", aliases = "null", help = "When a builder returns null for input.")
    public BuilderModule builderReturnedNull() {
        return new BuilderModule(null);
    }
}

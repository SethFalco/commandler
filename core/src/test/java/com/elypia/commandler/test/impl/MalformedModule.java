package com.elypia.commandler.test.impl;

import com.elypia.commandler.*;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.metadata.ModuleData;

/**
 * This is for testing if Commandler will correctly
 * reject a module that has everything set up correctly
 * except it's missing the {@link Module} annotation.
 */
public class MalformedModule extends Handler<String, String> {

    /**
     * Initialise the module, this will assign the values
     * in the module and create a {@link ModuleData} which is
     * what {@link Commandler} uses in runtime to identify modules,
     * commands or obtain any static data.
     *
     * @param commandler Our parent Commandler class.
     * @return Returns if the {@link #test()} for this module passed.
     */
    public MalformedModule(Commandler<String, String> commandler) {
        super(commandler);
    }

    @Command(id = "No Annotation", aliases = "anno", help = "A command that can never be triggered.")
    public String nomodule() {
        return "This module should fail to load due to no module annotation.";
    }
}

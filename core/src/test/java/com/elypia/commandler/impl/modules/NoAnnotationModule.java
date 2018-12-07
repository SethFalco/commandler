package com.elypia.commandler.impl.modules;

import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.impl.TestHandler;

/**
 * This is for testing if Commandler will correctly
 * reject a module that has everything set up correctly
 * except it's missing the {@link Module} annotation.
 */
public class NoAnnotationModule extends TestHandler {

    @Command(name = "Module Name", aliases = "noanno", help = "A command that can never be triggered.")
    public String nomodule() {
        return "This module should fail to load due to no module annotation.";
    }
}

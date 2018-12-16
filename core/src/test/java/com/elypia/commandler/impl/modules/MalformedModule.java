package com.elypia.commandler.impl.modules;

import com.elypia.commandler.Handler;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.impl.builders.DefaultBuilder;

/**
 * This is for testing if Commandler will correctly
 * reject a module that has everything set up correctly
 * except it's missing the {@link Module} annotation.
 */
@Builders(DefaultBuilder.class)
public class MalformedModule extends Handler<String, String> {

    @Command(id = "No Annotation", aliases = "anno", help = "A command that can never be triggered.")
    public String nomodule() {
        return "This module should fail to load due to no module annotation.";
    }
}

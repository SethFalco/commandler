package com.elypia.commandler.string.modules;

import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.impl.IBuilder;
import com.elypia.commandler.string.StringHandler;
import com.elypia.commandler.string.client.StringClient;

/**
 * This is for when we try to process a command into a data-type
 * that we don't have a {@link IBuilder} for.
 */ // ? We just copy and pasted commands from some other module.
@Module(name = "No Builder", aliases = "nb")
public class NoBuilderModule extends StringHandler {

    @Command(name = "Client Info", aliases = "info", help = "I'll give you the total sum of a list of numbers!")
    public StringClient info() {
        return new StringClient();
    }
}

package com.elypia.commandler.impl.modules;

import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.impl.StringHandler;
import com.elypia.commandler.impl.client.StringClient;
import com.elypia.commandler.interfaces.IBuilder;

/**
 * This is for when we try to process a command into a data-type
 * that we don't have a {@link IBuilder} for.
 */
@Module(name = "No Builder", aliases = "nb")
public class NoBuilderModule extends StringHandler {

    @Command(name = "Client Info", aliases = "info", help = "I'll give you the total sum of a list of numbers!")
    public StringClient info() {
        return new StringClient();
    }
}

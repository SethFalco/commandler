package com.elypia.commandler.test.integration.impl.modules;

import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.interfaces.Handler;

import javax.inject.Singleton;

@Singleton
@Module(name = "Utilities", aliases = "utils", help = "A series of utilities.")
public class UtilsModule implements Handler {

    @Static
    @Command(name = "ping!", aliases = "ping", help = "Returns pong!")
    public String ping() {
        return "pong!";
    }
}

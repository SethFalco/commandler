package com.elypia.commandler.impl.modules;

import com.elypia.commandler.Commandler;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.impl.TestHandler;

/**
 * This is just a really basic test module with the bare minimum
 * when it comes to testing {@link Commandler}.
 */
@Module(name = "Test", aliases = "test", help = "Test generic functionality and if it works.")
public class MiscModule extends TestHandler {

    @Command(name = "Say", aliases = "say", help = "I'll repeat something you say!")
    @Param(name = "input", help = "What you want me to say!")
    public String say(String input) {
        return input;
    }

    @Command(name = "Repeat", aliases = "repeat", help = "Repeat some text multiple times.")
    @Param(name = "input", help = "What you want me to say!")
    @Param(name = "count", help = "The number of times I should say it!")
    public String repeat(String input, int count) {
        return input.repeat(count);
    }

    @Static
    @Command(name = "Ping!", aliases = "ping", help = "Check if the bot is alive!")
    public String ping() {
        return "pong!";
    }
}

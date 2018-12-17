package com.elypia.commandler.test.impl.modules;

import com.elypia.commandler.*;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.*;

/**
 * This is just a really basic test module with the bare minimum
 * when it comes to testing {@link Commandler}.
 */
@Module(id = "Miscellaneous", aliases = "misc", help = "Test generic functionality and if it works.")
public class MiscModule extends Handler<String, String> {

    @Command(id = "Say", aliases = "say", help = "I'll repeat something you say.")
    @Param(id = "input", help = "What you want me to say.")
    public String say(String input) {
        return input;
    }

    @Command(id = "Repeat", aliases = "repeat", help = "Repeat some text multiple times.")
    @Param(id = "input", help = "What you want me to say.")
    @Param(id = "count", help = "The number of times I should say it.")
    public String repeat(String input, int count) {
        return input.repeat(count);
    }

    @Static
    @Command(id = "Ping!", aliases = "ping", help = "Check if I am alive.")
    public String ping() {
        return "pong!";
    }
}

package com.elypia.commandler.string.modules;

import com.elypia.commandler.Commandler;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.string.StringHandler;
import org.apache.commons.lang3.StringUtils;

/**
 * This is just a really basic test module with the bare minimum
 * when it comes to testing {@link Commandler}.
 */
@Module(name = "Test", aliases = "test", help = "My test module.")
public class TestModule extends StringHandler {

    @Command(name = "Say", aliases = "say", help = "I'll repeat something you say!")
    @Param(name = "input", help = "What you want me to say!")
    public String say(String input) {
        return input;
    }

    @Command(name = "Repeat", aliases = "repeat", help = "Repeat some text multiple times.")
    @Param(name = "input", help = "What you want me to say!")
    @Param(name = "count", help = "The number of times I should say it!")
    public String repeat(String input, int count) {
        return StringUtils.repeat(input, count);
    }

    @Static
    @Command(name = "Ping!", aliases = "ping", help = "Check if the bot is alive!")
    public String ping() {
        return "pong!";
    }
}

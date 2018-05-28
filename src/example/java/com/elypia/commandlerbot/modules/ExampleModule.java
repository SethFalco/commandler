package com.elypia.commandlerbot.modules;

import com.elypia.commandler.CommandHandler;
import com.elypia.commandler.annotations.Command;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.Param;
import com.elypia.commandler.annotations.validation.Length;
import com.elypia.commandler.annotations.validation.Limit;

@Module(
    name = "Example Module for Demo",
    aliases = {"example", "ex"},
    description = "This module is made for demonstration and examples."
)
public class ExampleModule extends CommandHandler {

    @Command(aliases = "spam", help = "Repeat a message multiple times.")
    @Param(name = "input", help = "The text to repeat.")
    @Param(name = "times", help = "How many times to send this message.")
    public String[] multiMessage(@Length(min = 5, max = 100) String input, int times) {
        String[] array = new String[times];

        for (int i = 0; i < times; i++)
            array[i] = input;

        return array;
    }

    @Command(aliases = "length")
    @Param(name = "input", help = "Some random text to ensure is the right length.")
    public String limit(@Length(min = 0, max = 32) String input) {
        return "Well done, the text was between 0 and 32 characters.";
    }

    @Command(aliases = "limit")
    @Param(name = "value", help = "A random number.")
    public String limit(@Limit(min = 0, max = 32) int value) {
        return "Well done, the value was between 0 and 32.";
    }
}

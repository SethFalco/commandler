package com.elypia.commandlerbot.modules;

import com.elypia.commandler.CommandHandler;
import com.elypia.commandler.annotations.Command;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.Param;
import com.elypia.commandler.annotations.validation.Length;

@Module(
    name = "Example Module for Demo",
    aliases = {"example", "ex"},
    description = "This module is made for demonstration and examples."
)
public class ExampleModule extends CommandHandler {

    @Command(aliases = "spam", help = "Repeat a message multiple times.")
    @Param(name = "input", help = "")
    @Param(name = "times", help = "")
    public String[] spam(String input, int times) {
        String[] array = new String[times];

        for (int i = 0; i < times; i++)
            array[i] = input;

        return array;
    }

    @Command(aliases = "length")
    @Param(name = "input", help = "Some random text to ensure is the right length.")
    public String length(@Length(min = 0, max = 32) String input) {
        return "Well done, the text was between 0 and 32 characters.";
    }
}

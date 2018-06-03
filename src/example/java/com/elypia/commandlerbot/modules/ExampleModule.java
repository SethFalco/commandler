package com.elypia.commandlerbot.modules;

import com.elypia.commandler.CommandHandler;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.annotations.validation.param.Length;

@Module(name = "Example Module for Demo", aliases = {"example", "ex"}, description = "This module is made for demonstration and examples.")
public class ExampleModule extends CommandHandler {

    @Command(aliases = "spam", help = "Repeat a message multiple times.")
    @Param(name = "input", help = "The text to repeat.")
    @Param(name = "times", help = "The number of times to repeat this message.")
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

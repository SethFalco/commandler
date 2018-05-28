package com.elypia.commandlerbot.modules;

import com.elypia.commandler.CommandHandler;
import com.elypia.commandler.annotations.Command;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.Param;

@Module(
    name = "Example Module for Demo",
    aliases = {"example", "ex"},
    description = "This module is made for demonstration and examples."
)
public class ExampleModule extends CommandHandler {

    @Command(aliases = "spam", help = "Repeat a message multiple times.")
    @Param(name = "input", help = "The text to repeat.")
    @Param(name = "times", help = "How many times to send this message.")
    public String[] multiMessage(String input, int times) {
        String[] array = new String[times];

        for (int i = 0; i < times; i++)
            array[i] = input;

        return array;
    }
}

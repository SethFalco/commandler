package com.elypia.commandlerbot.modules;

import com.elypia.commandler.annotations.*;
import com.elypia.commandler.annotations.validation.command.Elevated;
import com.elypia.commandler.annotations.validation.param.*;
import com.elypia.commandler.modules.CommandHandler;

@Elevated
@Module(name = "Example Module for Dev Demo", aliases = {"example", "ex"}, description = "Example module to demonstarte functionality.", hidden = true)
public class ExampleModule extends CommandHandler {

    @Command(name = "Spam the Chat", aliases = "spam", help = "Repeat a message multiple times.")
    @Param(name = "input", help = "The text to repeat.")
    @Param(name = "times", help = "The number of times to repeat this message.")
    public String[] spam(String input, int times) {
        String[] array = new String[times];

        for (int i = 0; i < times; i++)
            array[i] = input;

        return array;
    }

    @Command(name = "Validate Length of Input", aliases = "length")
    @Param(name = "input", help = "Some random text to ensure  it's the right length.")
    public String length(@Length(max = 32) String input) {
        return "Well done, the text was between 0 and 32 characters.";
    }

    @Command(name = "Validate Input is an Option", aliases = "option")
    @Param(name = "input", help = "A potential option from the list.")
    public String option(@Option({"user", "bot", "all"}) String accountType) {
        return "Well done, what you typed was a type of account.";
    }
}

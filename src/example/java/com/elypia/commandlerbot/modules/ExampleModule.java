package com.elypia.commandlerbot.modules;

import com.elypia.commandler.annotations.*;
import com.elypia.commandler.annotations.validation.command.Secret;
import com.elypia.commandler.annotations.validation.param.*;
import com.elypia.commandler.modules.CommandHandler;
import com.elypia.commandler.parsing.parsers.DurationParser;

import java.time.Duration;
import java.util.stream.IntStream;

@Module(name = "Example Module for Dev Demo", aliases = {"example", "ex"}, description = "Example module to demonstarte functionality.", hidden = true)
public class ExampleModule extends CommandHandler {

    @Command(name = "Spam the Chat", aliases = "spam", help = "Repeat a message multiple times.")
    @Param(name = "input", help = "The text to repeat.")
    @Param(name = "times", help = "The number of times to repeat this message.")
    public String[] spam(@Everyone String input, int times) {
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

    @Command(name = "Reminders", aliases = "reminder", help = "Set a reminder to trigger. (Exception it won't remind you.) <3")
    @Param(name = "text", help = "Should we perform this once or repeat it, specify with \"in\" or \"every\".")
    @Param(name = "time", help = "When to remind you!")
    public String length(@Option({"every", "in"}) String text, @Period(min = 3600) Duration time) {
        return "Thanks! I will remind you of nothing in this channel " + text + " " + DurationParser.forDisplay(time) + ".";
    }

    @Command(name = "Validate Input is an Option", aliases = "option")
    @Param(name = "type", help = "A potential option from the list.")
    public String option(@Option({"user", "bot", "all"}) String type) {
        return "Well done, what you typed was a type of account.";
    }

    @Secret
    @Command(name = "Tell me Password Pl0x", aliases = {"password", "pw"}, help = "Please tell me your password.")
    @Param(name = "password", help = "The password to your Discord account. :kappa:")
    public String password(String password) {
        return "Thank you!";
    }

    @Command(name = "List Parameters", aliases = "total", help = "Sum up all values provided.")
    @Param(name = "numbers", help = "A comma seperated list of all the numbers you want.")
    public int sum(int[] numbers) {
        return IntStream.of(numbers).sum();
    }
}

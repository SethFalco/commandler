package com.elypia.commandler.impl.modules;

import com.elypia.commandler.*;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.impl.builders.*;
import com.elypia.commandler.parsers.*;
import com.elypia.commandler.validation.*;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.*;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * This is testing if using the javax.validation and custom
 * built in custom validators in {@link Commandler} work correctly.
 */
@Parsers({StringParser.class, NumberParser.class, DurationParser.class})
@Builders({DefaultBuilder.class, NumberBuilder.class})
@Module(id = "Validation", aliases = "valid")
public class ValidationModule extends Handler<Void, String, String> {

    @Command(id = "Concatenate", aliases = "concat")
    @Param(id = "first", help = "The text that's prepended to the next.")
    @Param(id = "second", help = "The text that's appended to the previous.")
    public String concat(@Size(max = 1) String first, @Size(min = 1, max = 10) String second) {
        return first + second;
    }

    @Command(id = "Min & Max", aliases = "sum")
    @Param(id = "numbers", help = "A list of all numbers to sum together.")
    public long sumArray(@Max(100) int... numbers) {
        return IntStream.of(numbers).sum();
    }

    @Overload("Min & Max")
    @Param(id = "x", help = "The first number to sum.")
    @Param(id = "y", help = "The second number to sum.")
    public long sumTwoNumbers(@Min(1) int one, @Min(1) @Max(10) int two) {
        return sumArray(one, two);
    }

    @Command(id = "Sethi or Jenni", aliases = "panda")
    @Param(id = "id", help = "The better panda, Sethi or Jenni.")
    public String selectBetterPanda(@Option({"seth", "jen"}) String name) {
        return "You selected " + StringUtils.capitalize(name) + ".";
    }

    @Command(id = "Period", aliases = "period")
    @Param(id = "time", help = "Any period of time under two days.")
    public String getTime(@Period(max = 2, unit = TimeUnit.DAYS) Duration duration) {
        return String.valueOf(duration.toSeconds());
    }
}

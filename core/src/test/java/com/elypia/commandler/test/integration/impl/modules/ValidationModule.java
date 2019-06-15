package com.elypia.commandler.test.integration.impl.modules;

import com.elypia.commandler.Commandler;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.constraints.*;
import com.elypia.commandler.interfaces.Handler;

import javax.validation.constraints.*;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * This is testing if using the javax.validation and custom
 * built in custom validators in {@link Commandler} work correctly.
 */
@Module(name = "Validation", aliases = "valid")
public class ValidationModule implements Handler {

    @Command(name = "Concatenate", aliases = "concat")
    public String concat(
        @Param(name = "first", help = "The text that's prepended to the next.") @Size(max = 1) String first,
        @Param(name = "second", help = "The text that's appended to the previous.") @Size(min = 1, max = 10) String second
    ) {
        return first + second;
    }

    @Command(name = "Min & Max", aliases = "sum")
    public long sumArray(
        @Param(name = "numbers", help = "A list of all numbers to sum together.") @Max(100) int[] numbers
    ) {
        return IntStream.of(numbers).sum();
    }

    @Command(name = "Sethi or Jenni", aliases = "panda")
    public String selectBetterPanda(
        @Param(name = "id", help = "The better panda, Sethi or Jenni.") @Option({"seth", "jen"}) String name
    ) {
        return "You selected " + name.substring(0, 1).toUpperCase() + name.substring(1) + ".";
    }

    @Command(name = "Period", aliases = "period")
    public String getTime(
        @Param(name = "duration", help = "Any period of time under two days.") @Period(max = 2, unit = TimeUnit.DAYS) Duration duration
    ) {
        return duration.toSeconds() + " seconds";
    }
}

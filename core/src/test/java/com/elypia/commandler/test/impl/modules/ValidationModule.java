package com.elypia.commandler.test.impl.modules;

import com.elypia.commandler.Commandler;
import com.elypia.commandler.Handler;
import com.elypia.commandler.annotations.Command;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.Param;
import com.elypia.commandler.metadata.ModuleData;
import com.elypia.commandler.validation.Option;
import com.elypia.commandler.validation.Period;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.Max;
import javax.validation.constraints.Size;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * This is testing if using the javax.validation and custom
 * built in custom validators in {@link Commandler} work correctly.
 */
@Module(id = "Validation", aliases = "valid")
public class ValidationModule extends Handler<String, String> {

    /**
     * Initialise the module, this will assign the values
     * in the module and create a {@link ModuleData} which is
     * what {@link Commandler} uses in runtime to identify modules,
     * commands or obtain any static data.
     *
     * @param commandler Our parent Commandler class.
     */
    public ValidationModule(Commandler<String, String> commandler) {
        super(commandler);
    }

    @Command(id = "Concatenate", aliases = "concat")


    public String concat(
        @Param(id = "first", help = "The text that's prepended to the next.") @Size(max = 1) String first,
        @Param(id = "second", help = "The text that's appended to the previous.") @Size(min = 1, max = 10) String second
    ) {
        return first + second;
    }

    @Command(id = "Min & Max", aliases = "sum")
    public long sumArray(
        @Param(id = "numbers", help = "A list of all numbers to sum together.") @Max(100) int[] numbers
    ) {
        return IntStream.of(numbers).sum();
    }

    @Command(id = "Sethi or Jenni", aliases = "panda")
    public String selectBetterPanda(
        @Param(id = "id", help = "The better panda, Sethi or Jenni.") @Option({"seth", "jen"}) String name
    ) {
        return "You selected " + StringUtils.capitalize(name) + ".";
    }

    @Command(id = "Period", aliases = "period")
    public String getTime(
        @Param(id = "duration", help = "Any period of time under two days.") @Period(max = 2, unit = TimeUnit.DAYS) Duration duration
    ) {
        return duration.toSeconds() + " seconds";
    }
}

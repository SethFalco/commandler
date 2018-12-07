package com.elypia.commandler.impl.modules;

import com.elypia.commandler.Commandler;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.impl.TestHandler;
import com.elypia.commandler.validation.*;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.*;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * This is testing if using the javax.validation and custom
 * built in custom validators in {@link Commandler} work correctly.
 */
@Module(name = "Validation", aliases = "valid")
public class ValidationModule extends TestHandler {

    @Command(name = "Concatenate", aliases = "concat")
    @Param(name = "first", help = "The text that's prepended to the next.")
    @Param(name = "second", help = "The text that's appended to the previous.")
    public String concat(@Size(max = 1) String first, @Size(min = 1, max = 10) String second) {
        return first + second;
    }

    @Command(name = "Min & Max", aliases = "sum")
    @Param(name = "numbers", help = "A list of all numbers to sum together.")
    public long sumArray(@Max(100) int... numbers) {
        return IntStream.of(numbers).sum();
    }

    @Overload("Min & Max")
    @Param(name = "x", help = "The first number to sum.")
    @Param(name = "y", help = "The second number to sum.")
    public long sumTwoNumbers(@Min(1) int one, @Min(1) @Max(10) int two) {
        return sumArray(one, two);
    }

    @Command(name = "Sethi or Jenni", aliases = "panda")
    @Param(name = "name", help = "The better panda, Sethi or Jenni.")
    public String selectBetterPanda(@Option({"seth", "jen"}) String name) {
        return "You selected " + StringUtils.capitalize(name) + ".";
    }

    @Command(name = "Period", aliases = "period")
    @Param(name = "time", help = "Any period of time under two days.")
    public String underADay(@Period(max = 2, unit = TimeUnit.DAYS) Duration duration) {
        List<String> result = new ArrayList<>();
        TimeUnit base = TimeUnit.SECONDS;
        long unit = duration.getSeconds();

        long days = TimeUnit.DAYS.convert(unit, base);
        if (days > 0) {
            result.add(String.format("%,d %s", days, days == 1 ? "day" : "days"));
            unit -= base.convert(days, TimeUnit.DAYS);
        }

        long hours = TimeUnit.HOURS.convert(unit, base);
        if (hours > 0) {
            result.add(String.format("%,d %s", hours, hours == 1 ? "hour" : "hours"));
            unit -= base.convert(hours, TimeUnit.HOURS);
        }

        long minutes = TimeUnit.MINUTES.convert(unit, base);
        if (minutes > 0) {
            result.add(String.format("%,d %s", minutes, minutes == 1 ? "minute" : "minutes"));
            unit -= base.convert(minutes, TimeUnit.MINUTES);
        }

        if (unit != 0)
            result.add(String.format("%,d %s", unit, unit == 1 ? "second" : "seconds"));

        if (result.size() == 1) {
            String s = result.get(0);

            if (s.startsWith("1 hour"))
                s = s.replace("1 ", "an ");
            else if (s.startsWith("1 "))
                s = s.replace("1 ", "a ");

            return s;
        }

        Iterator<String> iter = result.iterator();
        StringJoiner joiner = new StringJoiner(", ");

        while (iter.hasNext()) {
            String it = iter.next();

            if (iter.hasNext())
                joiner.add(it);
            else
                joiner.add("and " + it);
        }

        return joiner.toString();
    }
}

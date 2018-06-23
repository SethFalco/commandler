package com.elypia.commandler.parsing.parsers;

import com.elypia.commandler.data.SearchScope;
import com.elypia.commandler.events.MessageEvent;
import com.elypia.commandler.parsing.IParamParser;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.*;

public class DurationParser implements IParamParser<Duration> {

    private Pattern matchPattern = Pattern.compile("(?i)\\A(?:\\d+\\s*[A-Z]+(?:\\s*,\\s*|\\s*)?)+\\Z");
    private Pattern splitPattern = Pattern.compile("(?i)(?<time>\\d+)\\s*(?<unit>[A-Z]+)");

    @Override
    public Duration parse(MessageEvent event, SearchScope scope, String input) {
        if (matchPattern.matcher(input).matches()) {
            Duration duration = Duration.ZERO;
            Matcher split = splitPattern.matcher(input);

            while (split.find()) {
                try {
                    long time = Long.parseLong(split.group("time"));
                    String unit = split.group("unit");
                    duration = addDuration(duration, time, unit);

                    if (duration == null) {
                        event.invalidate("One of the units provided where not valid, please only specify days, hours, minutes, seconds, milliseconds, and nanoseconds.");
                        return null;
                    }
                } catch (ArithmeticException | NumberFormatException ex) {
                    event.invalidate("I can't process numbers that large! Stahp breaking me!");
                    return null;
                }
            }

            return duration;
        }

        event.invalidate("Was unable to parse the input: `" + input + "` as a duration.");
        return null;
    }

    public static String forDisplay(Duration duration) {
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

    private Duration addDuration(Duration duration, long time, String unit) throws ArithmeticException {
        switch (unit.toLowerCase()) {
            case "days": case "day": case "d":
                return duration.plusDays(time);

            case "hours": case "hour": case "h":
                return duration.plusHours(time);

            case "minutes": case "minute": case "min": case "m":
                return duration.plusMinutes(time);

            case "seconds": case "second": case "secs": case "sec": case "s":
                return duration.plusSeconds(time);

            case "milliseconds": case "millisecond": case "millisecs": case "millisec": case "millis": case "milli": case "ms":
                return duration.plusMillis(time);

            case "nanoseconds": case "nanosecond": case "nanosecs": case "nanosec": case "nanos": case "nano": case "ns":
                return duration.plusNanos(time);

            default:
                return null;
        }
    }
}

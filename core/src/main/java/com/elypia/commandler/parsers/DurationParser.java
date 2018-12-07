package com.elypia.commandler.parsers;

import com.elypia.commandler.interfaces.*;

import java.time.Duration;
import java.util.regex.*;

public class DurationParser implements IParser<ICommandEvent, Duration> {

    private Pattern matchPattern = Pattern.compile("(?i)\\A(?:\\d+\\s*[A-Z]+(?:\\s*,\\s*|\\s*)?)+\\Z");
    private Pattern splitPattern = Pattern.compile("(?i)(?<time>\\d+)\\s*(?<unit>[A-Z]+)");

    @Override
    public Duration parse(ICommandEvent event, Class<? extends Duration> type, String input) {
        if (matchPattern.matcher(input).matches()) {
            Duration duration = Duration.ZERO;
            Matcher split = splitPattern.matcher(input);

            while (split.find()) {
                try {
                    long time = Long.parseLong(split.group("time"));
                    String unit = split.group("unit");
                    duration = addDuration(duration, time, unit);

                    if (duration == null)
                        return null;
                } catch (ArithmeticException | NumberFormatException ex) {
                    return null;
                }
            }

            return duration;
        }

        return null;
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

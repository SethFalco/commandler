package com.elypia.commandler.adapters;

import com.elypia.commandler.annotations.Compatible;
import com.elypia.commandler.interfaces.Adapter;
import com.elypia.commandler.metadata.data.ParamData;

import javax.inject.Singleton;
import java.text.*;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.*;

@Singleton
@Compatible(Duration.class)
public class DurationAdapter implements Adapter<Duration> {

    private static final TimeUnit[] units = {
        TimeUnit.DAYS,
        TimeUnit.HOURS,
        TimeUnit.MINUTES,
        TimeUnit.SECONDS,
        TimeUnit.MILLISECONDS,
        TimeUnit.NANOSECONDS
    };

    private static final Pattern splitPattern = Pattern.compile("(?i)\\s*(?<time>[^A-Z]+?)\\s*(?<unit>[A-Z]+)\\s*");

    private final NumberFormat formatter;
    private final TimeUnitAdapter timeUnitAdapter;

    public DurationAdapter() {
        this(NumberFormat.getInstance());
    }

    public DurationAdapter(NumberFormat formatter) {
        this(formatter, new TimeUnitAdapter(units));
    }

    public DurationAdapter(NumberFormat formatter, TimeUnitAdapter timeUnitAdapter) {
        this.formatter = Objects.requireNonNull(formatter);
        this.timeUnitAdapter = Objects.requireNonNull(timeUnitAdapter);
    }

    @Override
    public Duration adapt(String input, Class<? extends Duration> type, ParamData data) {
        Map<TimeUnit, Long> units = new HashMap<>();
        Matcher split = splitPattern.matcher(input);

        while (split.find()) {
            try {
                String timeInput = split.group("time");
                long time = formatter.parse(timeInput).longValue();
                String unitInput = split.group("unit");
                TimeUnit unit = timeUnitAdapter.adapt(unitInput);

                units.put(unit, time);
            } catch (IllegalArgumentException | ArithmeticException | ParseException ex) {
                return null;
            }
        }

        Duration duration = Duration.ZERO;

        for (Map.Entry<TimeUnit, Long> entry : units.entrySet()) {
            TimeUnit unit = entry.getKey();
            long time = entry.getValue();

            switch (unit) {
                case DAYS:
                    duration = duration.plusDays(time);
                    break;
                case HOURS:
                    duration = duration.plusHours(time);
                    break;
                case MINUTES:
                    duration = duration.plusMinutes(time);
                    break;
                case SECONDS:
                    duration = duration.plusSeconds(time);
                    break;
                case MILLISECONDS:
                    duration = duration.plusMillis(time);
                    break;
                case NANOSECONDS:
                    duration = duration.plusNanos(time);
                    break;
                default:
                    throw new IllegalStateException("Invalid timeunit provided.");
            }
        }

        return duration;
    }
}

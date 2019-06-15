package com.elypia.commandler.adapters;

import com.elypia.commandler.annotations.Compatible;
import com.elypia.commandler.interfaces.Adapter;
import com.elypia.commandler.metadata.data.MetaParam;

import javax.inject.Singleton;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Singleton
@Compatible(TimeUnit.class)
public class TimeUnitAdapter implements Adapter<TimeUnit> {

    private final Collection<TimeUnit> units;

    public TimeUnitAdapter() {
        this(TimeUnit.values());
    }

    public TimeUnitAdapter(TimeUnit... units) {
        this(List.of(units));
    }

    public TimeUnitAdapter(Collection<TimeUnit> units) {
        this.units = Objects.requireNonNull(units);
    }

    @Override
    public TimeUnit adapt(String input, Class<? extends TimeUnit> type, MetaParam param) {
        switch (input.toLowerCase()) {
            case "days": case "day": case "d":
                return (units.contains(TimeUnit.DAYS)) ? TimeUnit.DAYS : null;

            case "hours": case "hour": case "h":
                return (units.contains(TimeUnit.HOURS)) ? TimeUnit.HOURS : null;

            case "minutes": case "minute": case "min": case "m":
                return (units.contains(TimeUnit.MINUTES)) ? TimeUnit.MINUTES : null;

            case "seconds": case "second": case "secs": case "sec": case "s":
                return (units.contains(TimeUnit.SECONDS)) ? TimeUnit.SECONDS : null;

            case "milliseconds": case "millisecond": case "millisecs": case "millisec": case "millis": case "milli": case "ms":
                return (units.contains(TimeUnit.MILLISECONDS)) ? TimeUnit.MILLISECONDS : null;

            case "microseconds": case "microsecond": case "microsec": case "micros": case "micro": case "mic": case "mis":
                return (units.contains(TimeUnit.MICROSECONDS)) ? TimeUnit.MICROSECONDS : null;

            case "nanoseconds": case "nanosecond": case "nanosecs": case "nanosec": case "nanos": case "nano": case "ns":
                return (units.contains(TimeUnit.NANOSECONDS)) ? TimeUnit.NANOSECONDS : null;

            default:
                return null;
        }
    }
}

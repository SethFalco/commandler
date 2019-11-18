/*
 * Copyright 2019-2019 Elypia CIC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.elypia.commandler.adapters;

import org.elypia.commandler.api.Adapter;
import org.elypia.commandler.event.ActionEvent;
import org.elypia.commandler.metadata.MetaParam;

import javax.inject.Inject;
import java.text.*;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Adapt user input into Java {@link Duration} objects.
 * (This will allow users to specify a duration of time.)
 *
 * @author seth@elypia.org (Seth Falco)
 */
public class DurationAdapter implements Adapter<Duration> {

    /** The TimeUnits this is compatible with. */
    private static final TimeUnit[] units = {
        TimeUnit.DAYS,
        TimeUnit.HOURS,
        TimeUnit.MINUTES,
        TimeUnit.SECONDS,
        TimeUnit.MILLISECONDS,
        TimeUnit.NANOSECONDS
    };

    /** Java NumberFormatter instance, this has a locale set so we know how to parse it. */
    private final NumberFormat format;

    /** Uses the default {@link TimeUnitAdapter} to get the units after numbers. */
    private final TimeUnitAdapter timeUnitAdapter;

    /**
     * Instantiate the DurationAdapter with a NumberFormat with the default Locale.
     */
    public DurationAdapter() {
        this(NumberFormat.getInstance());
    }

    /** Instantiate the DurationAdapter with the default TimeUnitAdapter. */
    public DurationAdapter(NumberFormat format) {
        this(format, new TimeUnitAdapter(units));
    }

    @Inject
    public DurationAdapter(NumberFormat format, TimeUnitAdapter timeUnitAdapter) {
        this.format = Objects.requireNonNull(format);
        this.timeUnitAdapter = Objects.requireNonNull(timeUnitAdapter);
    }

    /**
     * Uses the NumberFormat to parse the number from the start,
     * then look for characters matching <code>(?i)[A-Z ]+</code>
     * to discover the timeunit, and repeat.
     *
     * @param input The input from the user.
     * @param type The type of data required.
     * @param data The parameter this is returning too.
     * @param event The event that required this parameter adapted.
     * @return The Duration object this represents, or null if it failed to parse.
     */
    @Override
    public Duration adapt(String input, Class<? extends Duration> type, MetaParam data, ActionEvent<?, ?> event) {
        Map<TimeUnit, Long> units = new HashMap<>();
        ParsePosition position = new ParsePosition(0);
        char[] sequence = input.toCharArray();

        while (!isFinished(input, position)) {
            Number number = format.parse(input, position);
            int start = position.getIndex();
            int end = start;

            while (end < sequence.length) {
                char c = sequence[end];

                if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == ' ')
                    end++;
                else
                    break;
            }

            position.setIndex(end);
            String unitString = input.substring(start, end);
            TimeUnit unit = timeUnitAdapter.adapt(unitString);

            if (unit == null)
                return null;

            if (units.containsKey(unit))
                units.put(unit, units.get(unit) + number.longValue());
            else
                units.putIfAbsent(unit, number.longValue());
        }

        if (units.size() == 0)
            return null;

        return buildDuration(units);
    }

    /**
     * Returns true if an error occured in parsing, or it just went through all
     * of the input and finished.
     *
     * @param input The string being parsed.
     * @param position The parse position being used against this string.
     * @return If this input and parse position are done parsing regardless of reason.
     */
    private boolean isFinished(String input, ParsePosition position) {
        return position.getErrorIndex() != -1 || position.getIndex() == input.length();
    }

    /**
     * Take the map if timeunits to units and put it together into a
     * Duration object which reflects the total time.
     *
     * @param units Each timeunit mapped against the value of time.
     * @return A duration object which puts all timeunits and values together.
     */
    private Duration buildDuration(Map<TimeUnit, Long> units) {
        Duration duration = Duration.ZERO;

        for (Map.Entry<TimeUnit, Long> entry : units.entrySet()) {
            long time = entry.getValue();

            switch (entry.getKey()) {
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

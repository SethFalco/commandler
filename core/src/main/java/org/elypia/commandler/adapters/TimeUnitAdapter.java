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

import javax.inject.Singleton;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author seth@elypia.org (Seth Falco)
 */
@Singleton
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
    public TimeUnit adapt(String input, Class<? extends TimeUnit> type, MetaParam metaParam, ActionEvent<?, ?> event) {
        Objects.requireNonNull(input);

        switch (input.trim().toLowerCase()) {
            case "days": case "day": case "d":
                return (units.contains(TimeUnit.DAYS)) ? TimeUnit.DAYS : null;

            case "hours": case "hour": case "h":
                return (units.contains(TimeUnit.HOURS)) ? TimeUnit.HOURS : null;

            case "minutes": case "minute": case "mins": case "min": case "m":
                return (units.contains(TimeUnit.MINUTES)) ? TimeUnit.MINUTES : null;

            case "seconds": case "second": case "secs": case "sec": case "s":
                return (units.contains(TimeUnit.SECONDS)) ? TimeUnit.SECONDS : null;

            case "milliseconds": case "millisecond": case "millisecs": case "millisec": case "millis": case "milli": case "ms":
                return (units.contains(TimeUnit.MILLISECONDS)) ? TimeUnit.MILLISECONDS : null;

            case "microseconds": case "microsecond": case "microsec": case "micros": case "micro": case "mic": case "mis":
                return (units.contains(TimeUnit.MICROSECONDS)) ? TimeUnit.MICROSECONDS : null;

            case "nanoseconds": case "nanosecond": case "nanosecs": case "nanosec": case "nanos": case "nano": case "ns": case "n":
                return (units.contains(TimeUnit.NANOSECONDS)) ? TimeUnit.NANOSECONDS : null;

            default:
                return null;
        }
    }
}

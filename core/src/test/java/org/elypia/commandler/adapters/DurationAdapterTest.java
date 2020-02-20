/*
 * Copyright 2019-2020 Elypia CIC
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

import org.junit.jupiter.api.Test;

import java.text.NumberFormat;
import java.time.Duration;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author seth@elypia.org (Seth Falco)
 */
public class DurationAdapterTest {

    @Test
    public void assertDurations() {
        DurationAdapter adapter = new DurationAdapter(NumberFormat.getInstance(Locale.UK));

        assertAll("Check if all these become the correct duration.",
            () -> assertEquals(Duration.ofDays(10), adapter.adapt("10 days")),
            () -> assertEquals(Duration.ofMinutes(70), adapter.adapt("1 hour 10 minutes"))
        );
    }

    @Test
    public void assertDurationsFormatted() {
        DurationAdapter adapter = new DurationAdapter(NumberFormat.getInstance(Locale.ITALY));

        assertAll("Check if formatted input returns the correct duration.",
            () -> assertEquals(Duration.ofHours(1001), adapter.adapt("1.001 hours")),
            () -> assertEquals(Duration.ofSeconds(3600), adapter.adapt("3.600 seconds")),
            () -> assertEquals(Duration.ofSeconds(3600), adapter.adapt("3600 seconds"))
        );
    }

    @Test
    public void testNullWithPartialValid() {
        DurationAdapter adapter = new DurationAdapter();

        assertAll("Check if partially valid input returns null.",
            () -> assertNull(adapter.adapt("100 hours invalid"))
        );
    }

    @Test
    public void testNull() {
        DurationAdapter adapter = new DurationAdapter();

        assertAll("Check if all these return null.",
            () -> assertNull(adapter.adapt("invalid")),
            () -> assertNull(adapter.adapt("100 invalid")),
            () -> assertNull(adapter.adapt("100")),
            () -> assertNull(adapter.adapt("this isn't even close to valid input")),
            () -> assertNull(adapter.adapt(""))
        );
    }

    @Test
    public void testMilliseconds() {
        DurationAdapter adapter = new DurationAdapter();

        Duration expected = Duration.ofMillis(123);
        Duration actual = adapter.adapt("123 ms");

        assertEquals(expected, actual);
    }

    @Test
    public void testNanoseconds() {
        DurationAdapter adapter = new DurationAdapter();

        Duration expected = Duration.ofNanos(512);
        Duration actual = adapter.adapt("512 ns");

        assertEquals(expected, actual);
    }

    @Test
    public void testDuplicateTimeUnits() {
        DurationAdapter adapter = new DurationAdapter();

        Duration expected = Duration.ofSeconds(45);
        Duration actual = adapter.adapt("30 seconds 15s");

        assertEquals(expected, actual);
    }
}

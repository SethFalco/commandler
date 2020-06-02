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

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author seth@elypia.org (Seth Falco)
 */
public class TimeUnitAdapterTest {

    private static TimeUnitAdapter adapter;

    @BeforeEach
    public void beforeEach() {
        adapter = new TimeUnitAdapter();
    }

    @Test
    public void assertSingle() {
        TimeUnit expected = TimeUnit.SECONDS;
        TimeUnit actual = adapter.adapt("sec");

        assertEquals(expected, actual);
    }

    @Test
    public void assertTimeUnits() {
        assertAll("Check that they all return the correct type.",
            () -> assertEquals(TimeUnit.SECONDS,      adapter.adapt("seconds")),
            () -> assertEquals(TimeUnit.HOURS,        adapter.adapt("h")),
            () -> assertEquals(TimeUnit.MICROSECONDS, adapter.adapt("mic")),
            () -> assertEquals(TimeUnit.MINUTES,      adapter.adapt("mins")),
            () -> assertEquals(TimeUnit.NANOSECONDS,  adapter.adapt("n")),
            () -> assertEquals(TimeUnit.SECONDS,      adapter.adapt("secs"))
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"aaaa", "invalid", "can't be done"})
    public void testNull(String value) {
        assertNull(adapter.adapt(value));
    }
}

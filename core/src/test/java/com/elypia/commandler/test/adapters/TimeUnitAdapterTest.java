package com.elypia.commandler.test.adapters;

import com.elypia.commandler.adapters.TimeUnitAdapter;
import org.junit.jupiter.api.*;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class TimeUnitAdapterTest {

    private static TimeUnitAdapter adapter;

    @BeforeEach
    public void beforeEach() {
        adapter = new TimeUnitAdapter();
    }

    @Test
    public void assertChars() {
        assertAll("Check that they all return the correct type.",
            () -> assertEquals(TimeUnit.SECONDS, adapter.adapt("seconds")),
            () -> assertEquals(TimeUnit.HOURS, adapter.adapt("h")),
            () -> assertEquals(TimeUnit.MICROSECONDS, adapter.adapt("mic")),
            () -> assertEquals(TimeUnit.MINUTES, adapter.adapt("mins")),
            () -> assertEquals(TimeUnit.NANOSECONDS, adapter.adapt("n")),
            () -> assertEquals(TimeUnit.SECONDS, adapter.adapt("secs"))
        );
    }

    @Test
    public void testNull() {
        assertAll("Check if all these return null.",
            () -> assertNull(adapter.adapt("aaaa")),
            () -> assertNull(adapter.adapt("invalid")),
            () -> assertNull(adapter.adapt("can't be done"))
        );
    }
}

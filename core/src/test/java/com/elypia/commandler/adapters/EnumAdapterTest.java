package com.elypia.commandler.adapters;

import org.junit.jupiter.api.*;

import java.time.DayOfWeek;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class EnumAdapterTest {

    private static EnumAdapter adapter;

    @BeforeEach
    public void beforeEach() {
        adapter = new EnumAdapter();
    }

    @Test
    public void assertEnums() {
        assertAll("Check if all these return the enum constant.",
            () -> assertEquals(DayOfWeek.FRIDAY, adapter.adapt("friday", DayOfWeek.class)),
            () -> assertEquals(TimeUnit.SECONDS, adapter.adapt("seconds", TimeUnit.class))
        );
    }

    @Test
    public void testNull() {
        assertAll("Check if all these return null.",
            () -> assertNull(adapter.adapt("invalid", DayOfWeek.class))
        );
    }
}

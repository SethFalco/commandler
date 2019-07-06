package com.elypia.commandler.test.adapters;

import com.elypia.commandler.adapters.DurationAdapter;
import org.junit.jupiter.api.Test;

import java.text.NumberFormat;
import java.time.Duration;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

public class DurationAdapterTest {

    @Test
    public void assertDurations() {
        DurationAdapter adapter = new DurationAdapter(NumberFormat.getInstance(Locale.UK));

        assertAll("Check if all these the correct duration.",
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

    // TODO: Uncomment this test
//    @Test
//    public void testNullWithPartialValid() {
//        DurationAdapter adapter = new DurationAdapter();
//
//        assertAll("Check if partially valid input returns null.",
//            () -> assertNull(adapter.adapt("100 hours invalid"))
//        );
//    }

    @Test
    public void testNull() {
        DurationAdapter adapter = new DurationAdapter();

        assertAll("Check if all these return null.",
            () -> assertNull(adapter.adapt("invalid")),
            () -> assertNull(adapter.adapt("100 invalid")),
            () -> assertNull(adapter.adapt("this isn't even close to valid input"))
        );
    }
}

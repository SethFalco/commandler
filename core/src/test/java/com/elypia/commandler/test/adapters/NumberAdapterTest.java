package com.elypia.commandler.test.adapters;

import com.elypia.commandler.adapters.NumberAdapter;
import org.junit.jupiter.api.Test;

import java.text.NumberFormat;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

public class NumberAdapterTest {

    @Test
    public void assertNumbers() {
        NumberAdapter adapter = new NumberAdapter();

        assertAll("Check if all these return the number equivilent.",
            () -> assertEquals(20, adapter.adapt("20")),
            () -> assertEquals(-500, adapter.adapt("-500")),
            () -> assertEquals(2147, adapter.adapt("2147"))
        );
    }

    @Test
    public void assertItaly() {
        NumberAdapter adapter = new NumberAdapter(NumberFormat.getInstance(Locale.ITALY));

        assertAll("Check if all these return the number equivilent.",
            () -> assertEquals(20000.456, adapter.adapt("20.000,456", Double.class)),
            () -> assertEquals(-1234345, adapter.adapt("-1.234.345"))
        );
    }

    @Test
    public void assertUk() {
        NumberAdapter adapter = new NumberAdapter(NumberFormat.getInstance(Locale.UK));

        assertAll("Check if all these return the number equivilent.",
            () -> assertEquals(20000.456, adapter.adapt("20,000.456", Double.class)),
            () -> assertEquals(-1234345, adapter.adapt("-1,234,345"))
        );
    }

    /** Tests cases where it starts with a number, but non-numeric text after. */
    @Test
    public void assertPartialMatch() {
        NumberAdapter adapter = new NumberAdapter(NumberFormat.getInstance(Locale.UK));

        assertAll("Check if all these return the number equivilent.",
            () -> assertNull(adapter.adapt("20,000.456 no u", Double.class)),
            () -> assertNull(adapter.adapt("-1,234,345pewpew"))
        );
    }

    @Test
    public void testNull() {
        NumberAdapter adapter = new NumberAdapter();

        assertAll("Check if all these return null.",
            () -> assertNull(adapter.adapt("invalid"))
        );
    }
}

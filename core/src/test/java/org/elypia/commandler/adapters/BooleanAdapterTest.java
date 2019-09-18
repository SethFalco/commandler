package org.elypia.commandler.adapters;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class BooleanAdapterTest {

    private static BooleanAdapter adapter;

    @BeforeEach
    public void beforeEach() {
        adapter = new BooleanAdapter();
    }

    @Test
    public void testTruthy() {
        assertAll("Check if all these return true.",
            () -> assertTrue(adapter.adapt("true")),
            () -> assertTrue(adapter.adapt("1"))
        );
    }

    @Test
    public void testFalsy() {
        assertAll("Check if all these return false.",
            () -> assertFalse(adapter.adapt("false")),
            () -> assertFalse(adapter.adapt("0"))
        );
    }

    @Test
    public void testNull() {
        assertAll("Check if all these return null.",
            () -> assertNull(adapter.adapt("neither")),
            () -> assertNull(adapter.adapt("-1"))
        );
    }
}

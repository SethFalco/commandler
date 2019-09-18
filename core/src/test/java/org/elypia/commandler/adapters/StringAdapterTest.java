package org.elypia.commandler.adapters;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class StringAdapterTest {

    private static StringAdapter adapter;

    @BeforeEach
    public void beforeEach() {
        adapter = new StringAdapter();
    }

    @Test
    public void assertStrings() {
        assertAll("Check the same text provided.",
            () -> assertEquals("10000", adapter.adapt("10000")),
            () -> assertEquals("Hello, world!", adapter.adapt("Hello, world!"))
        );
    }
}

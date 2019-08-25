package com.elypia.commandler.adapters;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class CharAdapterTest {

    private static CharAdapter adapter;

    @BeforeEach
    public void beforeEach() {
        adapter = new CharAdapter();
    }

    @Test
    public void assertChars() {
        assertAll("Check if all these return the character equivilent..",
            () -> assertEquals('A', adapter.adapt("A")),
            () -> assertEquals('1', adapter.adapt("1")),
            () -> assertEquals(',', adapter.adapt(","))
        );
    }

    @Test
    public void assert2Digits() {
        assertAll("Check if all these return character from the ASCII table.",
            () -> assertEquals('A', adapter.adapt("65")),
            () -> assertEquals('z', adapter.adapt("122"))
        );
    }

    @Test
    public void testNull() {
        assertAll("Check if all these return null.",
            () -> assertNull(adapter.adapt("128")),
            () -> assertNull(adapter.adapt("invalid")),
            () -> assertNull(adapter.adapt("can't be done"))
        );
    }
}

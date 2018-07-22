package com.elypia.commandler.string.test;

import com.elypia.commandler.string.StringCommandler;
import com.elypia.commandler.string.client.*;
import com.elypia.commandler.string.modules.ArrayModule;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ArrayModuleTest {

    private static StringCommandler commandler;

    @BeforeAll
    public static void beforeAll() {
        commandler = new StringCommandler(new StringClient(), ">");
        commandler.registerModule(new ArrayModule());
    }

    @Test
    public void testSum() {
        StringEvent event = new StringEvent(">array sum 1, 2, 3, 4");

        String response = commandler.trigger(event, event.getContent());
        assertEquals("10", response);
    }

    @Test
    public void testSumWithMultipler() {
        StringEvent event = new StringEvent(">array sum 1, 2, 3, 4 4");

        String response = commandler.trigger(event, event.getContent());
        assertEquals("40", response);
    }

    @Test
    public void testSumWrong() {
        StringEvent event = new StringEvent(">array sum 1, 2, 3, 4 4 4");

        String expected = "You specified the 'Add Numbers' command in the 'Array' module but the parameters weren't what I expected.\n\nProvided:\n(3) '1', '2', '3', '4' | '4' | '4'\n\nPossibilities:\n(1) ['numbers']\n(2) ['numbers'] | 'multiplier'";
        String actual = commandler.trigger(event, event.getContent());
        assertEquals(expected, actual);
    }

    @Test
    public void testSumObject() {
        StringEvent event = new StringEvent(">array sumo 1, 2, 3, 4, 5");

        String response = commandler.trigger(event, event.getContent());
        assertEquals("15", response);
    }
}

package com.elypia.commandler.console.test;

import com.elypia.commandler.console.StringCommandler;
import com.elypia.commandler.console.client.*;
import com.elypia.commandler.console.modules.ArrayModule;

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

        String expected = "Command failed; you provided the wrong number of parameters.\nModule: Array\nCommand: Add Numbers\n\nProvided:\n(3) ['1', '2', '3', '4'], '4', '4'\n\nPossibilities:\n(1) ['numbers']\n(2) ['numbers'], 'multiplier'";
        String actual = commandler.trigger(event, event.getContent());

        assertEquals(expected, actual);
    }

    @Test
    public void testSumObject() {
        StringEvent event = new StringEvent(">array sumo 1, 2, 3, 4, 5");

        String response = commandler.trigger(event, event.getContent());
        assertEquals("15", response);
    }

    // ? Just try each type of primitive array

    @Test
    public void testBoolSum() {
        StringEvent event = new StringEvent(">array boolsum true, true, true, false, false, true");

        String response = commandler.trigger(event, event.getContent());
        assertEquals("4 true, 2 false", response);
    }

    @Test void testCharSum() {
        StringEvent event = new StringEvent(">array chars h, e, l, l, o, w, o, r, l, d");

        String expected = "helloworld";
        String actual = commandler.trigger(event, event.getContent());

        assertEquals(expected, actual);
    }

    @Test
    public void testDoubles() {
        StringEvent event = new StringEvent(">array doubles 1, 2, 3, 4");

        String response = commandler.trigger(event, event.getContent());
        assertEquals("10", response);
    }

    @Test
    public void testFloats() {
        StringEvent event = new StringEvent(">array floats 1, 2, 3, 4");

        String response = commandler.trigger(event, event.getContent());
        assertEquals("10", response);
    }

    @Test
    public void testLongs() {
        StringEvent event = new StringEvent(">array longs 1, 2, 3, 4");

        String response = commandler.trigger(event, event.getContent());
        assertEquals("10", response);
    }

    @Test
    public void testShorts() {
        StringEvent event = new StringEvent(">array shorts 1, 2, 3, 4");

        String response = commandler.trigger(event, event.getContent());
        assertEquals("10", response);
    }

    @Test
    public void testBytes() {
        StringEvent event = new StringEvent(">array bytes 1, 2, 3, 4");

        String response = commandler.trigger(event, event.getContent());
        assertEquals("10", response);
    }
}

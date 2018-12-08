package com.elypia.commandler.test;

import com.elypia.commandler.impl.TestApp;
import com.elypia.commandler.impl.modules.ArrayModule;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ArrayTest {

    private static TestApp app;

    @BeforeAll
    public static void beforeAll() {
        app = new TestApp();
        app.add(ArrayModule.class);
    }

    @Test
    public void testSum() {
        String expected = "10";
        String actual = app.execute(">array sum 1, 2, 3, 4");

        assertEquals(expected, actual);
    }

    @Test
    public void testSumWithMultipler() {
        String expected = "40";
        String actual = app.execute(">array sum 1, 2, 3, 4 4");

        assertEquals(expected, actual);
    }

    @Test
    public void testSumWrong() {

        String expected = "Command failed; you provided the wrong number of parameters.\nModule: Array\nCommand: Add Numbers\n\nProvided:\n(3) ['1', '2', '3', '4'], '4', '4'\n\nPossibilities:\n(1) ['numbers']\n(2) ['numbers'], 'multiplier'";
        String actual = app.execute((">array sum 1, 2, 3, 4 4 4");

        assertEquals(expected, actual);
    }

    @Test
    public void testSumObject() {

        String actual = app.execute(">array sumo 1, 2, 3, 4, 5");
        assertEquals(expected, actual);
    }

    @Test
    public void testBoolSum() {

        String actual = app.execute(">array boolsum true, true, true, false, false, true");
        assertEquals(expected, actual);
    }

    @Test void testCharSum() {

        String expected = "helloworld";
        String actual = app.execute(">array chars h, e, l, l, o, w, o, r, l, d");

        assertEquals(expected, actual);
    }

    @Test
    public void testDoubles() {

        String actual = app.execute(">array doubles 1, 2, 3, 4");
        assertEquals(expected, actual);
    }

    @Test
    public void testFloats() {

        String actual = app.execute(">array floats 1, 2, 3, 4");
        assertEquals(expected, actual);
    }

    @Test
    public void testLongs() {

        String actual = app.execute(">array longs 1, 2, 3, 4");
        assertEquals(expected, actual);
    }

    @Test
    public void testShorts() {

        String actual = app.execute(">array shorts 1, 2, 3, 4");
        assertEquals(expected, actual);
    }

    @Test
    public void testBytes() {

        String actual = app.execute(">array bytes 1, 2, 3, 4");
        assertEquals(expected, actual);
    }
}

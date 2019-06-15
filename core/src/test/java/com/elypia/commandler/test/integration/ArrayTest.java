package com.elypia.commandler.test.integration;

import com.elypia.commandler.metadata.ContextLoader;
import com.elypia.commandler.metadata.loader.AnnotationLoader;
import com.elypia.commandler.test.integration.impl.builders.*;
import com.elypia.commandler.test.integration.impl.modules.ArrayModule;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ArrayTest {

    private static TestCommandler commandler;

    @BeforeAll
    public static void beforeAll() {
        ContextLoader loader = new ContextLoader(new AnnotationLoader());

        loader.add(
            ArrayModule.class,
            DefaultResponseProvider.class,
            NumberResponseProvider.class
        );

        commandler = new TestCommandlerBuilder()
            .setPrefix(">")
            .setContextLoader(loader)
            .build();
    }

    @Test
    public void testSum() {
        String expected = "10";
        String actual = commandler.execute(">array sum 1, 2, 3, 4");

        assertEquals(expected, actual);
    }

    @Test
    public void testSumWithMultipler() {
        String expected = "40";
        String actual = commandler.execute(">array sum 1, 2, 3, 4 4");

        assertEquals(expected, actual);
    }

    @Test
    public void testSumParamCountMismatch() {
        String expected =
            "Command failed; you provided the wrong number of parameters.\n" +
            "Module: Array\n" +
            "Command: Add Ints\n" +
            "\n" +
            "Provided:\n" +
            "(3) ['1', '2', '3', '4'], '4', '4'\n" +
            "\n" +
            "Possibilities:\n" +
            "(1) ['numbers']\n" +
            "(2) ['numbers'], 'multiplier'";

        String actual = commandler.execute(">array sum 1, 2, 3, 4 4 4");

        assertEquals(expected, actual);
    }

    @Test
    public void testSumIntegerObject() {
        String expected = "15";
        String actual = commandler.execute(">array sumo 1, 2, 3, 4, 5");

        assertEquals(expected, actual);
    }

    @Test
    public void testBoolCount() {
        String expected = "4 true, 2 false.";
        String actual = commandler.execute(">array bools true, true, true, false, false, true");

        assertEquals(expected, actual);
    }

    @Test void testSpell() {
        String expected = "helloworld";
        String actual = commandler.execute(">array spell h, e, l, l, o, w, o, r, l, d");

        assertEquals(expected, actual);
    }

    @Test
    public void testDoubles() {
        String expected = "10.4";
        String actual = commandler.execute(">array doubles 1.1, 2.1, 3.1, 4.1");

        assertEquals(expected, actual);
    }

    @Test
    public void testFloats() {
        String expected = "10";
        String actual = commandler.execute(">array floats 1, 2, 3, 4");

        assertEquals(expected, actual);
    }

    @Test
    public void testLongs() {
        String expected = "10";
        String actual = commandler.execute(">array longs 1, 2, 3, 4");

        assertEquals(expected, actual);
    }

    @Test
    public void testShorts() {
        String expected = "10";
        String actual = commandler.execute(">array shorts 1, 2, 3, 4");

        assertEquals(expected, actual);
    }

    @Test
    public void testBytes() {
        String expected = "10";
        String actual = commandler.execute(">array bytes 1, 2, 3, 4");

        assertEquals(expected, actual);
    }
}

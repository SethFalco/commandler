package com.elypia.commandler.string.test;

import com.elypia.commandler.string.StringCommandler;
import com.elypia.commandler.string.client.*;
import com.elypia.commandler.string.modules.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

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
    public void testSumObject() {
        StringEvent event = new StringEvent(">array sumo 1, 2, 3, 4, 5");

        String response = commandler.trigger(event, event.getContent());
        assertEquals("15", response);
    }
}

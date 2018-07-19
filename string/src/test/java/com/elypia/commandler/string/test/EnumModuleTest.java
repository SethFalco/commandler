package com.elypia.commandler.string.test;

import com.elypia.commandler.string.StringCommandler;
import com.elypia.commandler.string.client.*;
import com.elypia.commandler.string.modules.EnumModule;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class EnumModuleTest {

    private static StringCommandler commandler;

    @BeforeAll
    public static void beforeAll() {
        commandler = new StringCommandler(new StringClient(), ">");
        commandler.registerModule(new EnumModule());
    }

    @Test
    public void testTimeUnit() {
        StringEvent event = new StringEvent(">enum time seconds");

        String response = commandler.trigger(event, event.getContent());
        assertEquals("SECONDS", response);
    }

    @Test
    public void testTimeUnitInvalid() {
        StringEvent event = new StringEvent(">enum time hitler");

        String response = commandler.trigger(event, event.getContent());
        assertNull(response);
    }
}

package com.elypia.commandler.console.test;

import com.elypia.commandler.console.StringCommandler;
import com.elypia.commandler.console.client.*;
import com.elypia.commandler.console.modules.EnumModule;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

        String expected = "Command failed; I couldn't interpret 'hitler', as the parameter 'unit' (A unit of time!).\nModule: Enum\nCommand: TimeUnit\n\nRequired:\n(1) 'unit'";
        String actual = commandler.trigger(event, event.getContent());

        assertEquals(expected, actual);
    }
}

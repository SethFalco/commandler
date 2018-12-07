package com.elypia.commandler.test;

import com.elypia.commandler.impl.TestApp;
import com.elypia.commandler.impl.modules.ValidationModule;
import org.junit.jupiter.api.BeforeAll;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EnumModuleTest {

    private static TestApp app;

    @BeforeAll
    public static void beforeAll() {
        app = new TestApp();
        app.add(ValidationModule.class);
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

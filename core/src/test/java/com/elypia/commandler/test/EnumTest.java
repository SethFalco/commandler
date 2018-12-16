package com.elypia.commandler.test;

import com.elypia.commandler.test.impl.TestApp;
import com.elypia.commandler.test.impl.modules.EnumModule;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EnumTest {

    private static TestApp app;

    @BeforeAll
    public static void beforeAll() {
        app = new TestApp();
        app.add(EnumModule.class);
    }

    @Test
    public void testTimeUnit() {
        String expected = "SECONDS";
        String actual = app.execute(">enum timeunit seconds");

        assertEquals(expected, actual);
    }

    @Test
    public void testInvalidEnum() {
        String expected = "Command failed; I couldn't interpret 'pyrocynical', as the parameter 'youtuber' (The number one YouTuber.).\nModule: Enum\nCommand: Top YouTuber\n\nRequired:\n(1) 'youtuber'";
        String actual = app.execute(">enum top pyrocynical");

        assertEquals(expected, actual);
    }
}

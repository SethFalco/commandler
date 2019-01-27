package com.elypia.commandler.test;

import com.elypia.commandler.test.impl.TestApp;
import com.elypia.commandler.test.impl.modules.ValidationModule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ValidationTest {

    private static TestApp app;

    @BeforeAll
    public static void beforeAll() {
        app = new TestApp();
        app.add(ValidationModule.class);
    }

    @Test
    public void testLength() {
        String expected = "ajenni";
        String actual = app.execute(">valid concat a jenni");

        assertEquals(expected, actual);
    }

    @Test
    public void testInvalidLength() {
        String expected =
            "Command failed; a parameter was invalidated.\n" +
            "Module: Validation\n" +
            "Command: Concatenate\n" +
            "\n" +
            "first: Size must be between 0 and 1.";

        String actual = app.execute(">valid concat an jenni");

        assertEquals(expected, actual);
    }

    @Test
    public void testOption() {
        String expected = "You selected Seth.";
        String actual = app.execute(">valid panda seth");

        assertEquals(expected, actual);
    }

    @Test
    public void testValidPeriod() {
        String expected = "3723 seconds";
        String actual = app.execute(">valid period 1h2m3s");

        assertEquals(expected, actual);
    }

    @Test
    public void testInvalidPeriod() {
        String expected =
            "Command failed; a parameter was invalidated.\n" +
            "Module: Validation\n" +
            "Command: Period\n" +
            "\n" +
            "duration: Must be between 0 and 2 days.";

        String actual = app.execute(">valid period 3d12h20s");

        assertEquals(expected, actual);
    }
}

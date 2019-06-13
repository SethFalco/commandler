package com.elypia.commandler.test.integration;

import com.elypia.commandler.metadata.ContextLoader;
import com.elypia.commandler.metadata.loader.AnnotationLoader;
import com.elypia.commandler.test.integration.impl.builders.*;
import com.elypia.commandler.test.integration.impl.modules.ValidationModule;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ValidationTest {

    private static TestCommandler commandler;

    @BeforeAll
    public static void beforeAll() {
        ContextLoader loader = new ContextLoader(new AnnotationLoader());
        loader.add(ValidationModule.class, DefaultProvider.class, NumberProvider.class);

        commandler = new TestCommandlerBuilder()
            .setPrefix(">")
            .setContextLoader(loader)
            .build();
    }

    @Test
    public void testLength() {
        String expected = "ajenni";
        String actual = commandler.execute(">valid concat a jenni");

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

        String actual = commandler.execute(">valid concat an jenni");

        assertEquals(expected, actual);
    }

    @Test
    public void testOption() {
        String expected = "You selected Seth.";
        String actual = commandler.execute(">valid panda seth");

        assertEquals(expected, actual);
    }

    @Test
    public void testValidPeriod() {
        String expected = "3723 seconds";
        String actual = commandler.execute(">valid period 1h2m3s");

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

        String actual = commandler.execute(">valid period 3d12h20s");

        assertEquals(expected, actual);
    }
}

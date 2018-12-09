package com.elypia.commandler.test;

import com.elypia.commandler.impl.TestApp;
import com.elypia.commandler.impl.modules.EnumModule;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MisuseTest {

    private static TestApp app;

    @BeforeAll
    public static void beforeAll() {
        app = new TestApp();
        app.add(EnumModule.class);
    }

    @Test
    public void onParamCountMismatch() {
        String expected = "Command failed; you provided the wrong number of parameters.\nModule: Enum\nCommand: TimeUnit\n\nProvided:\n(2) 'seconds', 'extra'\n\nPossibilities:\n(1) 'unit'";
        String actual = app.execute(">enum time seconds extra");

        assertEquals(expected, actual);
    }

    @Test
    public void onNoDefault() {
        String expected = "Command failed; this module has no default command.\nModule: Enum\n\nPossibilities:\nTimeUnit ('time')\n\nSee the help command for more information.";
        String actual = app.execute(">enum");

        assertEquals(expected, actual);
    }

    @Test
    public void onUnsupportedList() {
        String expected = "Command failed; the input, ['seconds', 'minutes', 'hours'], for parameter 'unit' can't be a list.\nModule: Enum\nCommand: TimeUnit\n\nRequired:\n(1) 'unit'";
        String actual = app.execute(">enum time seconds, minutes, hours");

        assertEquals(expected, actual);
    }
}

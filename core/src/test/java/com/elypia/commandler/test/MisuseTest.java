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
        String expected =
            "Command failed; you provided the wrong number of parameters.\n" +
            "Module: Enum\n" +
            "Command: TimeUnit\n" +
            "\n" +
            "Provided:\n" +
            "(2) 'seconds', 'extra'\n" +
            "\n" +
            "Possibilities:\n" +
            "(1) 'unit'";

        String actual = app.execute(">enum timeunit seconds extra");

        assertEquals(expected, actual);
    }

    @Test
    public void onNoDefault() {
        String expected =
            "Command failed; this module has no default command.\n" +
            "Module: Enum\n" +
            "\n" +
            "Possibilities:\n" +
            "TimeUnit ('timeunit')\n" +
            "Top YouTuber ('top')\n" +
            "\n" +
            "See the help command for more information.";

        String actual = app.execute(">enum");

        assertEquals(expected, actual);
    }

    @Test
    public void onUnsupportedList() {
        String expected =
            "Command failed; the input, ['seconds', 'minutes', 'hours'], for parameter 'unit' can't be a list.\n" +
            "Module: Enum\n" +
            "Command: TimeUnit\n" +
            "\n" +
            "Required:\n" +
            "(1) 'unit'";

        String actual = app.execute(">enum timeunit seconds, minutes, hours");

        assertEquals(expected, actual);
    }
}

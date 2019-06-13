package com.elypia.commandler.test.integration;

import com.elypia.commandler.metadata.ContextLoader;
import com.elypia.commandler.test.integration.impl.builders.DefaultProvider;
import com.elypia.commandler.test.integration.impl.modules.EnumModule;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MisuseTest {

    private static TestCommandler commandler;

    @BeforeAll
    public static void beforeAll() {
        ContextLoader loader = new ContextLoader(EnumModule.class);

        loader.add(
            DefaultProvider.class
        );

        commandler = new TestCommandlerBuilder()
            .setPrefix(">")
            .setContextLoader(loader)
            .build();
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

        String actual = commandler.execute(">enum timeunit seconds extra");

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
            "See the value command for more information.";

        String actual = commandler.execute(">enum");

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

        String actual = commandler.execute(">enum timeunit seconds, minutes, hours");

        assertEquals(expected, actual);
    }
}

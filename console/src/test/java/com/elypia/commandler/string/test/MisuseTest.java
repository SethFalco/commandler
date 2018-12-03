package com.elypia.commandler.string.test;

import com.elypia.commandler.string.StringCommandler;
import com.elypia.commandler.string.client.*;
import com.elypia.commandler.string.modules.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MisuseTest {

    private static StringCommandler commandler;

    @BeforeAll
    public static void beforeAll() {
        commandler = new StringCommandler(new StringClient(), ">");

        commandler.registerModules(
            new EnumModule(),
            new FailureModule()
        );
    }

    @Test
    public void onParamCountMismatch() {
        StringEvent event = new StringEvent(">enum time seconds extra");

        String expected = "Command failed; you provided the wrong number of parameters.\nModule: Enum\nCommand: TimeUnit\n\nProvided:\n(2) 'seconds', 'extra'\n\nPossibilities:\n(1) 'unit'";
        String actual = commandler.trigger(event, event.getContent());

        assertEquals(expected, actual);
    }

    @Test
    public void onNoDefault() {
        StringEvent event = new StringEvent(">enum");

        String expected = "Command failed; this module has no default command.\nModule: Enum\n\nPossibilities:\nTimeUnit ('time')\n\nSee the help command for more information.";
        String actual = commandler.trigger(event, event.getContent());

        assertEquals(expected, actual);
    }

    @Test
    public void onUnsupportedList() {
        StringEvent event = new StringEvent(">enum time seconds, minutes, hours");

        String expected = "Command failed; the input, ['seconds', 'minutes', 'hours'], for parameter 'unit' can't be a list.\nModule: Enum\nCommand: TimeUnit\n\nRequired:\n(1) 'unit'";
        String actual = commandler.trigger(event, event.getContent());

        assertEquals(expected, actual);
    }

    @Test
    public void onModuleDisabled() {
        StringEvent event = new StringEvent(">failure failure");

        String expected = "Command failed; this module is currently disabled due to live issues.\nModule: Failure";
        String actual = commandler.trigger(event, event.getContent());

        assertEquals(expected, actual);
    }
}

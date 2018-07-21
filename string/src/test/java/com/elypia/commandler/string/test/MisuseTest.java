package com.elypia.commandler.string.test;

import com.elypia.commandler.string.StringCommandler;
import com.elypia.commandler.string.client.*;
import com.elypia.commandler.string.modules.EnumModule;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MisuseTest {

    private static StringCommandler commandler;

    @BeforeAll
    public static void beforeAll() {
        commandler = new StringCommandler(new StringClient(), ">");
        commandler.registerModule(new EnumModule());
    }

    @Test
    public void onParamCountMismatch() {
        StringEvent event = new StringEvent(">enum time seconds extra");

        String expected = "You specified the 'TimeUnit' command in the 'Enum' module but the parameters weren't what I expected.\n\nProvided:\n(2) 'seconds' | 'extra'\n\nPossibilities:\n(1) 'unit'";
        String actual = commandler.trigger(event, event.getContent());
        assertEquals(expected, actual);
    }

    @Test
    public void onNoDefault() {
        StringEvent event = new StringEvent(">enum");

        String expected = "You specified the 'Enum' module without a valid command, however this module has no default command.\n\nPossibilities:\n(Help) 'help'\n(TimeUnit) 'time'\n\nSee the help command for more information.";
        String actual = commandler.trigger(event, event.getContent());
        assertEquals(expected, actual);
    }
}

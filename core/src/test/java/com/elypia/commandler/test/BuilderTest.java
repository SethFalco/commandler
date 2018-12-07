package com.elypia.commandler.test;

import com.elypia.commandler.console.StringCommandler;
import com.elypia.commandler.console.modules.NoBuilderModule;
import com.elypia.commandler.impl.TestApp;
import com.elypia.commandler.impl.builders.NumberBuilder;
import com.elypia.commandler.impl.modules.ValidationModule;
import com.elypia.commandler.parsers.NumberParser;
import org.junit.jupiter.api.BeforeAll;

import static org.junit.jupiter.api.Assertions.*;

public class BuilderTest {

    private static TestApp app;

    @BeforeAll
    public static void beforeAll() {
        app = new TestApp();
        app.add(ValidationModule.class);
    }

    @Test
    public void reregisterExistingType() {
        commandler.registerBuilder(new NumberBuilder(), NumberParser.TYPES);
    }

    @Test
    public void useCommandNoBuilder() {
        commandler.registerModule(new NoBuilderModule());
        StringEvent event = new StringEvent(">nb info");

        String expected = "An unknown error occured.";
        String actual = commandler.trigger(event, event.getContent());

        assertEquals(expected, actual);
    }

    @Test
    public void badBuilder() {
        commandler.registerModule(new NoBuilderModule());
        commandler.registerBuilder(new StringClientBuilder(), StringClient.class);

        StringEvent event = new StringEvent(">nb info");

        String expected = "An unknown error occured.";
        String actual = commandler.trigger(event, event.getContent());

        assertEquals(expected, actual);
    }

    /**
     * Ensure our default builders {@link StringCommandler} are registering
     * correctly.
     */
    @Test
    public void builderIteration() {
        assertNotNull(commandler.getBuilder().iterator().next());
    }
}

package com.elypia.commandler.test;

import com.elypia.commandler.console.StringCommandler;
import com.elypia.commandler.console.modules.NoBuilderModule;
import com.elypia.commandler.parsers.NumberParser;

import static org.junit.jupiter.api.Assertions.*;

public class BuilderTest {

    private StringCommandler commandler;

    @BeforeEach
    public void beforeEach() {
        commandler = new StringCommandler(new StringClient(), ">");
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

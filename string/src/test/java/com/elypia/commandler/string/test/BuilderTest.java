package com.elypia.commandler.string.test;

import com.elypia.commandler.annotations.*;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.impl.*;
import com.elypia.commandler.parsers.NumberParser;
import com.elypia.commandler.string.*;
import com.elypia.commandler.string.builders.NumberBuilder;
import com.elypia.commandler.string.client.*;
import org.junit.jupiter.api.*;

import static org.junit.Assert.*;

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
        commandler.registerBuilder(new IStringBuilder<StringClient>() {

            @Override
            public String build(StringCommand event, StringClient output) {
                return null;
            }
        }, StringClient.class);

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

    /**
     * This is for when we try to process a command into a data-type
     * that we don't have a {@link IBuilder} for.
     */ // ? We just copy and pasted commands from some other module.
    @Module(name = "No Builder", aliases = "nb")
    public class NoBuilderModule extends StringHandler {

        @Command(name = "Client Info", aliases = "info", help = "I'll give you the total sum of a list of numbers!")
        public StringClient info() {
            return new StringClient();
        }
    }
}

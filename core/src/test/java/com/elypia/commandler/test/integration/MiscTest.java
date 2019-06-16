package com.elypia.commandler.test.integration;

import com.elypia.commandler.meta.ContextLoader;
import com.elypia.commandler.meta.loaders.AnnotationLoader;
import com.elypia.commandler.test.integration.impl.builders.*;
import com.elypia.commandler.test.integration.impl.modules.MiscModule;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MiscTest {

    private static TestCommandler commandler;

    @BeforeAll
    public static void beforeAll() {
        ContextLoader loader = new ContextLoader(new AnnotationLoader());

        loader.add(
            MiscModule.class,
            DefaultResponseProvider.class,
            NumberResponseProvider.class
        );

        commandler = new TestCommandlerBuilder()
            .setPrefix(">")
            .setContextLoader(loader)
            .build();
    }

    @Test
    public void testSay() {
        String expected = "hi";
        String actual = commandler.execute(">misc say hi");

        assertEquals(expected, actual);
    }

    @Test
    public void testRepeat() {
        String expected = "hellohellohellohellohello";
        String actual = commandler.execute(">misc repeat hello 5");

        assertEquals(expected, actual);
    }

    @Test
    public void testStaticPing() {
        String expected = "pong!";
        String actual = commandler.execute(">ping");

        assertEquals(expected, actual);
    }

    @Test
    public void testHelp() {
        String expected =
            "Miscellaneous (misc)\n" +
            "Test generic functionality and if it works.\n" +
            "\n" +
            "Ping! (ping)\n" +
            "Check if I am alive.\n" +
            "\n" +
            "Repeat (repeat)\n" +
            "Repeat some text multiple times.\n" +
            "input: What you want me to say.\n" +
            "count: The number of times I should say it.\n" +
            "\n" +
            "Say (say)\n" +
            "I'll repeat something you say.\n" +
            "input: What you want me to say.";

        String actual = commandler.execute(">misc help");

        assertEquals(expected, actual);
    }
}

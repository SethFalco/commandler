package com.elypia.commandler.string.test;

import com.elypia.commandler.annotations.*;
import com.elypia.commandler.string.client.*;
import com.elypia.commandler.string.commandler.*;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestModuleTest {

    private static StringCommandler commandler;

    @BeforeAll
    public static void beforeAll() {
        commandler = new StringCommandler(new StringClient(), ">");
        commandler.registerModule(new TestModule());
    }

    @Test
    public void testSay() {
        StringEvent event = new StringEvent(">test say hi");

        String response = commandler.trigger(event, event.getContent());
        assertEquals("hi", response);
    }

    @Test
    public void testRepeat() {
        StringEvent event = new StringEvent(">test repeat hello 5");

        String response = commandler.trigger(event, event.getContent());
        assertEquals("hellohellohellohellohello", response);
    }

    @Test
    public void testStaticPing() {
        StringEvent event = new StringEvent(">ping");

        String response = commandler.trigger(event, event.getContent());
        assertEquals("pong!", response);
    }

    @Test
    public void testHelp() {
        StringEvent event = new StringEvent(">test help");

        String response = commandler.trigger(event, event.getContent());
        assertEquals("helped", response);
    }

    @Module(name = "Test", aliases = "test", description = "My test module.")
    public static class TestModule extends StringHandler {

        @Command(name = "Say", aliases = "say", help = "I'll repeat something you say!")
        @Param(name = "input", help = "What you want me to say!")
        public String say(String input) {
            return input;
        }

        @Command(name = "Repeat", aliases = "repeat", help = "Repeat some text multiple times.")
        @Param(name = "input", help = "What you want me to say!")
        @Param(name = "count", help = "The number of times I should say it")
        public String repeat(String input, int count) {
            return StringUtils.repeat(input, count);
        }

        @Static
        @Command(name = "Ping!", aliases = "ping", help = "Check if the bot is alive!")
        public String ping() {
            return "pong!";
        }
    }
}

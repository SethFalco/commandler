package com.elypia.commandler.string.test;

import com.elypia.commandler.string.*;
import com.elypia.commandler.string.client.*;
import com.elypia.commandler.string.modules.TestModule;
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

        String expected = "Test (test)\nMy test module.\n\nPing! (ping)\nCheck if the bot is alive!\n\nRepeat (repeat)\nRepeat some text multiple times.\ninput: What you want me to say!\ncount: The number of times I should say it!\n\nSay (say)\nI'll repeat something you say!\ninput: What you want me to say!";
        String actual = commandler.trigger(event, event.getContent());

        assertEquals(expected, actual);
    }
}

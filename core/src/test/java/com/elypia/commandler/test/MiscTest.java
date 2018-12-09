package com.elypia.commandler.test;

import com.elypia.commandler.impl.TestApp;
import com.elypia.commandler.impl.modules.MiscModule;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MiscTest {

    private static TestApp app;

    @BeforeAll
    public static void beforeAll() {
        app = new TestApp();
        app.add(MiscModule.class);
    }

    @Test
    public void testSay() {
        String expected = "hi";
        String actual = app.execute(">misc say hi");

        assertEquals(expected, actual);
    }

    @Test
    public void testRepeat() {
        String expected = "hellohellohellohellohello";
        String actual = app.execute(">misc repeat hello 5");

        assertEquals(expected, actual);
    }

    @Test
    public void testStaticPing() {
        String expected = "pong!";
        String actual = app.execute(">ping");

        assertEquals(expected, actual);
    }

    @Test
    public void testHelp() {
        String expected = "Test (test)\nMy test module.\n\nPing! (ping)\nCheck if the bot is alive!\n\nRepeat (repeat)\nRepeat some text multiple times.\ninput: What you want me to say!\ncount: The number of times I should say it!\n\nSay (say)\nI'll repeat something you say!\ninput: What you want me to say!";
        String actual = app.execute(">misc help");

        assertEquals(expected, actual);
    }
}

package com.elypia.commandler.string.test;

import com.elypia.commandler.annotations.*;
import com.elypia.commandler.modules.CommandHandler;
import com.elypia.commandler.string.*;
import com.elypia.commandler.string.building.builders.DefaultBuilder;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestModuleTest {

    private StringCommandler commandler;

    @BeforeAll
    public void beforeAll() {
        StringDispatcher dispatcher = new StringDispatcher();
        dispatcher.registerBuilder(String.class, new DefaultBuilder());

        StringCommandler commandler = new StringCommandler(">");
        commandler.setDispatcher(new StringDispatcher());

        commandler.registerModule(new TestModule());
    }

    @Test
    public void testSay() {
        String response = commandler.trigger(">test say hi");
        assertEquals("hi", response);
    }

    @Test
    public void testRepeat() {
        String response = commandler.trigger(">test repeat hello 5");
        assertEquals("hellohellohellohellohello", response);
    }

    @Module(name = "Test", aliases = "test", description = "My test module.")
    public class TestModule extends CommandHandler {

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
    }
}

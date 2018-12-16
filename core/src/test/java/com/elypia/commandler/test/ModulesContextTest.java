package com.elypia.commandler.test;

import com.elypia.commandler.ModulesContext;
import com.elypia.commandler.test.impl.TestApp;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ModulesContextTest {

    private static TestApp app;

    @BeforeAll
    public static void beforeAll() {
        app = new TestApp();
    }

    @Test
    public void testAddingPackage() {
        ModulesContext context = app.getCommandler().getContext();
        context.addPackage("com.elypia.commandler.test.impl.modules");

        int expected = 5;
        int actual = context.getModules().size();

        assertEquals(expected, actual);
    }
}

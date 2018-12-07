package com.elypia.commandler.test;

import com.elypia.commandler.impl.TestApp;
import com.elypia.commandler.impl.modules.ValidationModule;
import org.junit.jupiter.api.BeforeAll;

public class HandlerTest {

    private static TestApp app;

    @BeforeAll
    public static void beforeAll() {
        app = new TestApp();
        app.add(ValidationModule.class);
    }


    @Test
    public void clientInstance() {
        assertEquals(client, module.getClient());
    }

    @Test
    public void commandlerInstance() {
        assertEquals(commandler, module.getCommandler());
    }
}

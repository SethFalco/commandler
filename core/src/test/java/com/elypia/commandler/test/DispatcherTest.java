package com.elypia.commandler.test;

import com.elypia.commandler.impl.TestApp;
import com.elypia.commandler.impl.modules.ValidationModule;
import org.junit.jupiter.api.BeforeAll;

public class DispatcherTest {

    private static TestApp app;

    @BeforeAll
    public static void beforeAll() {
        app = new TestApp();
        app.add(ValidationModule.class);
    }

    @Test
    public void clientInstance() {
        assertEquals(client, commandler.getDispatcher().getClient());
    }
}

package com.elypia.commandler.console.test;

import com.elypia.commandler.console.StringCommandler;
import com.elypia.commandler.console.client.StringClient;
import com.elypia.commandler.console.modules.EnumModule;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HandlerTest {

    private static StringClient client;
    private static StringCommandler commandler;
    private static EnumModule module;

    @BeforeAll
    public static void beforeAll() {
        client = new StringClient();
        commandler = new StringCommandler(client, ">");
        module = new EnumModule();

        commandler.registerModule(module);
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

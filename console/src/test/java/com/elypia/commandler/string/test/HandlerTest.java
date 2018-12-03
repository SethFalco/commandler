package com.elypia.commandler.string.test;

import com.elypia.commandler.string.StringCommandler;
import com.elypia.commandler.string.client.*;
import com.elypia.commandler.string.modules.EnumModule;
import org.junit.jupiter.api.*;

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

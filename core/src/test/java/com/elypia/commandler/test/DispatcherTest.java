package com.elypia.commandler.test;

import com.elypia.commandler.console.StringCommandler;
import com.elypia.commandler.console.client.StringClient;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DispatcherTest {

    private static StringClient client;
    private static StringCommandler commandler;

    @BeforeAll
    public static void beforeAll() {
        client = new StringClient();
        commandler = new StringCommandler(client, ">");
    }

    @Test
    public void clientInstance() {
        assertEquals(client, commandler.getDispatcher().getClient());
    }
}

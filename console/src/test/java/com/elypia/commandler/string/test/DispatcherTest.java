package com.elypia.commandler.string.test;

import com.elypia.commandler.string.StringCommandler;
import com.elypia.commandler.string.client.StringClient;
import org.junit.jupiter.api.*;

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

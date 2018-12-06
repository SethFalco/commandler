package com.elypia.commandler.test;

import com.elypia.commandler.console.StringCommandler;
import com.elypia.commandler.console.client.StringClient;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class MalformedModuleTest {

    private StringCommandler commandler;

    @BeforeEach
    public void beforeEach() {
        commandler = new StringCommandler(new StringClient(), ">");
    }

    @Test
    public void noModuleAnnotation() {
        assertThrows(IllegalStateException.class, () -> {
            commandler.registerModule(new NoModuleModule());
        });
    }

    // ? This will throw an exception because we're registering the same aliases again
    @Test
    public void reregisterModule() {
        commandler.registerModule(new ArrayModule());

        assertThrows(IllegalStateException.class, () -> {
            commandler.registerModule(new ArrayModule());
        });
    }
}

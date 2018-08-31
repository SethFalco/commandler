package com.elypia.commandler.string.test;

import com.elypia.commandler.annotations.Command;
import com.elypia.commandler.string.*;
import com.elypia.commandler.string.client.StringClient;
import com.elypia.commandler.string.modules.*;
import org.junit.jupiter.api.*;

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

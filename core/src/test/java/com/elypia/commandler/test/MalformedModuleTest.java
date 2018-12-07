package com.elypia.commandler.test;

import com.elypia.commandler.impl.TestApp;
import com.elypia.commandler.impl.modules.*;
import org.junit.jupiter.api.BeforeAll;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class MalformedModuleTest {

    private static TestApp app;

    @BeforeAll
    public static void beforeAll() {
        app = new TestApp();
        app.add(ValidationModule.class);
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

package com.elypia.commandler.test;

import com.elypia.commandler.test.impl.*;
import com.elypia.commandler.test.impl.modules.ArrayModule;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class MalformedModuleTest {

    private static TestApp app;

    @BeforeAll
    public static void beforeAll() {
        app = new TestApp();
    }

    @Test
    public void noModuleAnnotation() {
        assertThrows(IllegalStateException.class, () -> {
            app.add(MalformedModule.class);
        });
    }

    @Test
    public void reregisterModule() {
        app.add(ArrayModule.class);

        assertThrows(IllegalStateException.class, () -> {
            app.add(ArrayModule.class);
        });
    }
}

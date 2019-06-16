package com.elypia.commandler.test.integration;

import com.elypia.commandler.meta.ContextLoader;
import com.elypia.commandler.meta.loaders.AnnotationLoader;
import com.elypia.commandler.test.integration.impl.modules.ArrayModule;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ContextTest {

    private static TestCommandler commandler;

    @BeforeAll
    public static void beforeAll() {
        ContextLoader loader = new ContextLoader(ArrayModule.class, new AnnotationLoader());
        commandler = new TestCommandlerBuilder()
            .setPrefix(">")
            .setContextLoader(loader)
            .build();
    }

    @Test
    public void testAddingPackage() {
        // TODO: Make the default DefHelpModule optional.
        int expected = 7;
        int actual = commandler.getContext().getModules().size();

        assertEquals(expected, actual);
    }
}

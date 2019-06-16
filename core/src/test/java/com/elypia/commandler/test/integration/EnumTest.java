package com.elypia.commandler.test.integration;

import com.elypia.commandler.meta.ContextLoader;
import com.elypia.commandler.meta.loaders.AnnotationLoader;
import com.elypia.commandler.test.integration.impl.builders.*;
import com.elypia.commandler.test.integration.impl.modules.EnumModule;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EnumTest {

    private static TestCommandler commandler;

    @BeforeAll
    public static void beforeAll() {
        ContextLoader loader = new ContextLoader(new AnnotationLoader());

        loader.add(
            EnumModule.class,
            DefaultResponseProvider.class,
            NumberResponseProvider.class
        );

        commandler = new TestCommandlerBuilder()
            .setPrefix(">")
            .setContextLoader(loader)
            .build();
    }

    @Test
    public void testTimeUnit() {
        String expected = "SECONDS";
        String actual = commandler.execute(">enum timeunit seconds");

        assertEquals(expected, actual);
    }

    @Test
    public void testInvalidEnum() {
        String expected = "Command failed; I couldn't interpret 'pyrocynical', as the parameter 'youtuber' (The number one YouTuber.).\nModule: Enum\nCommand: Top YouTuber\n\nRequired:\n(1) 'youtuber'";
        String actual = commandler.execute(">enum top pyrocynical");

        assertEquals(expected, actual);
    }
}

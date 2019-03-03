package com.elypia.commandler.test;

import com.elypia.commandler.metadata.ContextLoader;
import com.elypia.commandler.metadata.loader.AnnotationLoader;
import com.elypia.commandler.test.impl.*;
import com.elypia.commandler.test.impl.builders.*;
import com.elypia.commandler.test.impl.modules.EnumModule;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EnumTest {

    private static TestCommandler commandler;

    @BeforeAll
    public static void beforeAll() {
        ContextLoader loader = new ContextLoader(new AnnotationLoader());

        loader.add(
            EnumModule.class,
            DefaultBuilder.class,
            NumberBuilder.class
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

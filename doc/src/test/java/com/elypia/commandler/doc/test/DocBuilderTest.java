package com.elypia.commandler.doc.test;

import com.elypia.commandler.ModulesContext;
import com.elypia.commandler.doc.DocBuilder;
import com.elypia.commandler.doc.impl.*;
import org.junit.jupiter.api.*;

import java.io.IOException;

public class DocBuilderTest {

    private static DocBuilder builder;

    @BeforeAll
    public static void beforeAll() {
        builder = new DocBuilder("TestApp");
    }

    @Test
    public void testAll() throws IOException {
        ModulesContext context = new ModulesContext();

        context.addModules(
            MiscModule.class,
            OsuModule.class,
            RuneScapeModule.class,
            YouTubeModule.class
        );

        builder.setContext(context);
        builder.setLogo("https://elypia.com/resources/logo_pic.png");
        builder.build();
    }
}

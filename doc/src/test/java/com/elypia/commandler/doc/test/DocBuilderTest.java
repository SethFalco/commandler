package com.elypia.commandler.doc.test;

import com.elypia.commandler.ModulesContext;
import com.elypia.commandler.doc.DocBuilder;
import com.elypia.commandler.doc.impl.*;
import org.junit.jupiter.api.*;

import java.io.IOException;

public class DocBuilderTest {

    private static ModulesContext context;
    private static DocBuilder builder;

    @BeforeAll
    public static void beforeAll() {
        context = new ModulesContext();
        builder = new DocBuilder("Commandler", context);
    }

    @Test
    public void testAll() throws IOException {
        context.addModules(
            MiscModule.class,
            OsuModule.class,
            RuneScapeModule.class,
            YouTubeModule.class
        );

        builder
            .setDescription("A Test static website compiled from Commandler.")
            .setLogo("https://elypia.com/resources/logo_pic.png")
            .setFavicon("png", builder.getLogo())
            .build();
    }
}

package com.elypia.commandler.doc.test;

import com.elypia.commandler.ModulesContext;
import com.elypia.commandler.doc.*;
import com.elypia.commandler.doc.impl.*;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.util.List;

public class DocBuilderTest {

    private static ModulesContext context;
    private static DocBuilder builder;

    @BeforeAll
    public static void beforeAll() {
        context = new ModulesContext();
        builder = new DocBuilder();
    }

    @Test
    public void testAll() throws IOException {
        context.addModules(
            MiscModule.class,
            OsuModule.class,
            RuneScapeModule.class,
            YouTubeModule.class
        );

        List<SocialLink> socialLinks = List.of(
            new SocialLink("fab fa-discord", "https://discord.gg/hprGMaM", "purple"),
            new SocialLink( "fab fa-gitlab", "https://gitlab.com/Elypia", "orange"),
            new SocialLink("fab fa-twitter", "https://twitter.com/Elypia", "blue")
        );

        builder
            .setContext(context)
            .setName("TestApp")
            .setSocialLinks(socialLinks)
            .setDescription("A Test static website compiled from Commandler.")
            .setLogo("https://elypia.com/resources/logo_pic.png")
            .setFavicon("png", builder.getLogo())
            .build();
    }
}

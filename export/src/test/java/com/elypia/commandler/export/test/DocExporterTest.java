package com.elypia.commandler.export.test;

import com.elypia.commandler.ModulesContext;
import com.elypia.commandler.export.DocExporter;
import com.elypia.commandler.export.impl.*;
import org.junit.jupiter.api.*;

public class DocExporterTest {

    private static ModulesContext context;
    private static DocExporter exporter;

    @BeforeAll
    public static void beforeAll() {
        context = new ModulesContext();
        exporter = new DocExporter().setContext(context);
    }

    @Test
    public void exportTest() {
        context.addModules(MiscModule.class, OsuModule.class, RuneScapeModule.class, YouTubeModule.class);
        System.out.println(exporter.toJson());
    }
}

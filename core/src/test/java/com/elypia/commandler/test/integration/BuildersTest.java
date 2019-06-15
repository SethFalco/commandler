package com.elypia.commandler.test.integration;

import com.elypia.commandler.metadata.ContextLoader;
import com.elypia.commandler.metadata.loader.AnnotationLoader;
import com.elypia.commandler.test.integration.impl.builders.*;
import com.elypia.commandler.test.integration.impl.modules.BuilderModule;
import org.junit.jupiter.api.BeforeAll;

public class BuildersTest {

    private static TestCommandler commandler;

    @BeforeAll
    public static void beforeAll() {
        ContextLoader loader = new ContextLoader(new AnnotationLoader());

        loader.add(
            BuilderModule.class,
            DefaultResponseProvider.class,
            NumberResponseProvider.class
        );

        commandler = new TestCommandlerBuilder()
            .setPrefix(">")
            .setContextLoader(loader)
            .build();
    }
}

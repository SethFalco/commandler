package com.elypia.commandler.test;

import com.elypia.commandler.metadata.ContextLoader;
import com.elypia.commandler.metadata.loader.AnnotationLoader;
import com.elypia.commandler.test.impl.*;
import com.elypia.commandler.test.impl.builders.*;
import com.elypia.commandler.test.impl.modules.BuilderModule;
import org.junit.jupiter.api.BeforeAll;

public class BuildersTest {

    private static TestCommandler commandler;

    @BeforeAll
    public static void beforeAll() {
        ContextLoader loader = new ContextLoader(new AnnotationLoader());

        loader.add(
            BuilderModule.class,
            DefaultBuilder.class,
            NumberBuilder.class
        );

        commandler = new TestCommandlerBuilder()
            .setPrefix(">")
            .setContextLoader(loader)
            .build();
    }
}

package com.elypia.commandler.test;

import com.elypia.commandler.test.impl.TestApp;
import com.elypia.commandler.test.impl.modules.BuilderModule;
import org.junit.jupiter.api.BeforeAll;

public class BuildersTest {

    private static TestApp app;

    @BeforeAll
    public static void beforeAll() {
        app = new TestApp();
        app.add(BuilderModule.class);
    }
}

package com.elypia.commandler.test.integration;

import com.elypia.commandler.*;
import com.elypia.commandler.controllers.ConsoleController;
import com.elypia.commandler.interfaces.*;
import com.elypia.commandler.loader.AnnotationLoader;
import com.elypia.commandler.metadata.ContextLoader;
import com.elypia.commandler.providers.MiscToStringProvider;
import com.elypia.commandler.test.integration.impl.modules.UtilsModule;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class UtilsModuleTest {

    private static Dispatcher dispatcher;
    private static Controller controller;

    @BeforeEach
    public void beforeEach() {
        Context context = new ContextLoader(new AnnotationLoader(
            UtilsModule.class,
            MiscToStringProvider.class
        )).load().build();

        Commandler commandler = new Commandler(context);
        dispatcher = commandler.getDispatcher();
        controller = new ConsoleController(dispatcher);
    }

    @Test
    public void utilsPing() {
        String event = ">utils ping";
        Object actual = dispatcher.dispatch(controller, event, event);

        assertAll("Check all",
            () -> assertTrue(actual instanceof String),
            () -> assertEquals("pong!", actual)
        );
    }

    @Test
    public void staticUtilsPing() {
        String event = ">ping";
        Object actual = dispatcher.dispatch(controller, event, event);

        assertAll("Check all",
            () -> assertTrue(actual instanceof String),
            () -> assertEquals("pong!", actual)
        );
    }
}

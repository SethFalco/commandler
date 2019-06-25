package com.elypia.commandler.test.integration;

import com.elypia.commandler.*;
import com.elypia.commandler.adapters.*;
import com.elypia.commandler.controllers.ConsoleController;
import com.elypia.commandler.interfaces.*;
import com.elypia.commandler.loader.AnnotationLoader;
import com.elypia.commandler.metadata.ContextLoader;
import com.elypia.commandler.providers.*;
import com.elypia.commandler.test.integration.impl.modules.MathModule;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class MathModuleTest {

    private static Dispatcher dispatcher;
    private static Controller controller;

    @BeforeEach
    public void beforeEach() {
        Context context = new ContextLoader(new AnnotationLoader(
            MathModule.class,
            NumberAdapter.class,
            CharAdapter.class,
            NumberToStringProvider.class,
            MiscToStringProvider.class
        )).load().build();

        Commandler commandler = new Commandler(context);
        dispatcher = commandler.getDispatcher();
        controller = new ConsoleController(dispatcher);
    }

    @Test
    public void mathSum() {
        String event = ">math sum 1, 2, 3, 4";
        Object actual = dispatcher.dispatch(controller, event, event);

        assertAll("Check all",
            () -> assertTrue(actual instanceof String),
            () -> assertEquals("10", actual)
        );
    }

    @Test
    public void mathSumd() {
        String event = ">math sumd 1.1, 2.2, 3.3, 4.4";
        Object actual = dispatcher.dispatch(controller, event, event);

        assertAll("Check all",
            () -> assertTrue(actual instanceof String),
            () -> assertEquals("11", actual)
        );
    }

    @Test
    public void mathSumExtraParam() {
        String event = ">math sum 1, 2, 3, 4 true";
        Object expected =
            "Command failed: you provided the wrong number of parameters.\n" +
            "Module: Math\n" +
            "Command: Sum\n" +
            "Required: [numbers]\n" +
            "Provided: (2) ['1', '2', '3', '4'], 'true'";
        Object actual = dispatcher.dispatch(controller, event, event);

        assertAll("Check all",
            () -> assertTrue(actual instanceof String),
            () -> assertEquals(expected, actual)
        );
    }
}

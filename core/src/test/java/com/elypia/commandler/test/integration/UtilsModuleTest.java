package com.elypia.commandler.test.integration;

import com.elypia.commandler.*;
import com.elypia.commandler.adapters.StringAdapter;
import com.elypia.commandler.controllers.ConsoleController;
import com.elypia.commandler.dispatchers.CommandDispatcher;
import com.elypia.commandler.interfaces.Controller;
import com.elypia.commandler.loaders.AnnotationLoader;
import com.elypia.commandler.managers.DispatcherManager;
import com.elypia.commandler.metadata.ContextLoader;
import com.elypia.commandler.providers.MiscToStringProvider;
import com.elypia.commandler.test.integration.impl.modules.UtilsModule;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class UtilsModuleTest {

    private static DispatcherManager dispatcher;
    private static Controller controller;

    @BeforeEach
    public void beforeEach() {
        Context context = new ContextLoader(new AnnotationLoader(
            UtilsModule.class,
            StringAdapter.class,
            MiscToStringProvider.class
        )).load().build();

        Commandler commandler = new Commandler.Builder(context).build();
        dispatcher = commandler.getDispatcherManager();
        dispatcher.addDispatchers(new CommandDispatcher(commandler, ">"));

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

    @Test
    public void printListTest() {
        String event = ">utils list";
        Object actual = dispatcher.dispatch(controller, event, event);

        assertAll("Check all",
            () -> assertTrue(actual instanceof String),
            () -> assertEquals("It\nhelps\nto\nspecify\na\nlist!", actual)
        );
    }

    @Test
    public void printServiceName() {
        String event = ">utils service";
        Object actual = dispatcher.dispatch(controller, event, event);

        assertAll("Check all",
            () -> assertTrue(actual instanceof String),
            () -> assertEquals("ConsoleController", actual)
        );
    }
}

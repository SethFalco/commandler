package com.elypia.commandler.test.integration;

import com.elypia.commandler.*;
import com.elypia.commandler.adapters.*;
import com.elypia.commandler.controllers.ConsoleController;
import com.elypia.commandler.dispatchers.CommandDispatcher;
import com.elypia.commandler.interfaces.Controller;
import com.elypia.commandler.loaders.AnnotationLoader;
import com.elypia.commandler.managers.DispatcherManager;
import com.elypia.commandler.metadata.ContextLoader;
import com.elypia.commandler.providers.MiscToStringProvider;
import com.elypia.commandler.test.integration.impl.modules.MiscModule;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class MiscModuleTest {

    private static DispatcherManager dispatcher;
    private static Controller controller;

    @BeforeEach
    public void beforeEach() {
        Context context = new ContextLoader(new AnnotationLoader(
            MiscModule.class,
            StringAdapter.class,
            NumberAdapter.class,
            MiscToStringProvider.class
        )).load().build();

        Commandler commandler = new Commandler.Builder(context).build();
        dispatcher = commandler.getDispatcherManager();
        dispatcher.addDispatchers(new CommandDispatcher(commandler, ">"));

        controller = new ConsoleController(dispatcher);
    }

    @Test
    public void utilsSayString() {
        String event = ">misc say \"Hello, world!\"";
        Object actual = dispatcher.dispatch(controller, event, event);

        assertAll("Check all",
            () -> assertTrue(actual instanceof String),
            () -> assertEquals("Hello, world!", actual)
        );
    }

    @Test
    public void utilsSayStringNumber() {
        String event = ">misc say \"Hello, world!\" 2";
        Object actual = dispatcher.dispatch(controller, event, event);

        assertAll("Check all",
            () -> assertTrue(actual instanceof String),
            () -> assertEquals("Hello, world!Hello, world!", actual)
        );
    }

    @Test
    public void utilsWrongParameter() {
        String event = ">misc say Hello world";
        String expected =
            "Command failed; I couldn't interpret 'world', as the parameter 'count' (How many times?).\n" +
            "Module: Miscellaneous\n" +
            "Command: Repeat\n" +
            "Required: input, ?count\n" +
            "Provided: (2) 'Hello', 'world'";
        Object actual = dispatcher.dispatch(controller, event, event);

        assertAll("Check all",
            () -> assertTrue(actual instanceof String),
            () -> assertEquals(expected, actual)
        );
    }

    @Test
    public void utilsListAsParam() {
        String event = ">misc say Hello, world";
        String expected =
            "Command failed; the parameter 'input' can't be a list.\n" +
            "Module: Miscellaneous\n" +
            "Command: Repeat\n" +
            "Required: input, ?count\n" +
            "Provided: (1) ['Hello', 'world']";
        Object actual = dispatcher.dispatch(controller, event, event);

        assertAll("Check all",
            () -> assertTrue(actual instanceof String),
            () -> assertEquals(expected, actual)
        );
    }
}

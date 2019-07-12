package com.elypia.commandler.test.integration;

import com.elypia.commandler.*;
import com.elypia.commandler.adapters.*;
import com.elypia.commandler.controllers.ConsoleController;
import com.elypia.commandler.dispatchers.StandardDispatcher;
import com.elypia.commandler.loaders.AnnotationLoader;
import com.elypia.commandler.managers.DispatcherManager;
import com.elypia.commandler.metadata.ContextLoader;
import com.elypia.commandler.metadata.builders.ModuleBuilder;
import com.elypia.commandler.test.integration.impl.builders.NullBuilder;
import com.elypia.commandler.test.integration.impl.modules.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class MalformedModuleTest {

    @Test
    public void failToInit() {
        assertThrows(NullPointerException.class,
            () -> new ModuleBuilder(MalformedModule.class).build()
        );
    }

    @Test
    public void builderReturnsNull() {
        Context context = new ContextLoader(new AnnotationLoader(
            MiscModule.class,
            StringAdapter.class,
            NumberAdapter.class,
            NullBuilder.class
        )).load().build();

        Commandler commandler = new Commandler.Builder(context).build();
        DispatcherManager dispatcher = commandler.getDispatcherManager();
        dispatcher.add(new StandardDispatcher(commandler, ">"));
        ConsoleController controller = new ConsoleController(dispatcher);

        String event = ">misc say Test";
        assertThrows(IllegalStateException.class, () -> dispatcher.dispatch(controller, event, event));
    }
}

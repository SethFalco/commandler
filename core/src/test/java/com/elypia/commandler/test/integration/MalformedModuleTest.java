package com.elypia.commandler.test.integration;

import com.elypia.commandler.*;
import com.elypia.commandler.adapters.*;
import com.elypia.commandler.controllers.ConsoleController;
import com.elypia.commandler.interfaces.Dispatcher;
import com.elypia.commandler.loader.AnnotationLoader;
import com.elypia.commandler.metadata.ContextLoader;
import com.elypia.commandler.metadata.builder.ModuleBuilder;
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

        Commandler commandler = new Commandler(context);
        Dispatcher dispatcher = commandler.getDispatcher();
        ConsoleController controller = new ConsoleController(dispatcher);

        String event = ">misc say Test";
        assertThrows(IllegalStateException.class, () -> dispatcher.dispatch(controller, event, event));
    }
}

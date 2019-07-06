package com.elypia.commandler.test.meta;

import com.elypia.commandler.Context;
import com.elypia.commandler.loaders.AnnotationLoader;
import com.elypia.commandler.metadata.*;
import com.elypia.commandler.metadata.builders.ModuleBuilder;
import com.elypia.commandler.test.integration.impl.modules.MiscModule;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class ContextLoaderTest {

    @Test
    public void getLoaders() {
        ContextLoader loader = new ContextLoader(
            new AnnotationLoader(MiscModule.class)
        );

        assertFalse(loader.getMetaLoaders().isEmpty());
    }

    @Test
    public void load() {
        ContextLoader loader = new ContextLoader(
            new AnnotationLoader(MiscModule.class)
        ).load();

        assertAll("Check loader has correct number of special classes.",
            () -> assertEquals(1, loader.getModuleBuilders().size()),
            () -> assertTrue(loader.getAdapterBuilders().isEmpty()),
            () -> assertTrue(loader.getProviderBuilders().isEmpty())
        );
    }

    @Test
    public void moduleDetails() {
        ContextLoader loader = new ContextLoader(
            new AnnotationLoader(MiscModule.class)
        ).load();

        Collection<ModuleBuilder> modules = loader.getModuleBuilders();
        ModuleBuilder module = modules.iterator().next();

        assertAll("Check that the a module has the correct details.",
            () -> assertEquals(MiscModule.class, module.getHandlerType()),
            () -> assertNull(module.getGroup()),
            () -> assertEquals("Miscellaneous", module.getName()),
            () -> assertFalse(module.getAliases().isEmpty()),
            () -> assertEquals("Test generic functionality and if it works.", module.getHelp()),
            () -> assertEquals(1, module.getCommandBuilders().size())
        );
    }

    @Test
    public void buildModuleDetails() {
        Context context = new ContextLoader(
            new AnnotationLoader(MiscModule.class)
        ).load().build();

        MetaModule module = context.getModule(MiscModule.class);

        assertAll("Check that the a module has the correct details.",
            () -> assertEquals(MiscModule.class, module.getHandlerType()),
            () -> assertEquals("Miscellaneous", module.getGroup()),
            () -> assertEquals("Miscellaneous", module.getName()),
            () -> assertFalse(module.getAliases().isEmpty()),
            () -> assertEquals("Test generic functionality and if it works.", module.getHelp()),
            () -> assertEquals(1, module.getCommands().size())
        );
    }

    @Test
    public void buildCommandDetails() {
        Context context = new ContextLoader(
            new AnnotationLoader(MiscModule.class)
        ).load().build();

        MetaModule module = context.getModule(MiscModule.class);
        MetaCommand command = module.getCommands().get(0);

        assertAll("Verify MetaCommand has the data we're expecting.",
            () -> assertEquals("Repeat", command.getName()),
            () -> assertFalse(command.getAliases().isEmpty()),
            () -> assertEquals("Repeat text one or more times.", command.getHelp()),
            () -> assertEquals(2, command.getParams().size()),
            () -> assertEquals("input, ?count", command.toParamString())
        );
    }

    @Test
    public void buildParamDetails() {
        Context context = new ContextLoader(
            new AnnotationLoader(MiscModule.class)
        ).load().build();

        MetaModule module = context.getModule(MiscModule.class);
        MetaCommand command = module.getCommands().get(0);
        MetaParam param = command.getParams().get(0);

        assertAll("Verify ParamBuilder has the data we're expecting.",
            () -> assertEquals(String.class, param.getType()),
            () -> assertEquals("input", param.getName()),
            () -> assertEquals("What to say?", param.getHelp()),
            () -> assertNull(param.getDefaultValue())
        );
    }
}

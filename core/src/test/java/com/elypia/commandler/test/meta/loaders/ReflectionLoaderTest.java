package com.elypia.commandler.test.meta.loaders;

import com.elypia.commandler.loader.ReflectionLoader;
import com.elypia.commandler.metadata.builder.*;
import com.elypia.commandler.test.integration.impl.modules.MiscModule;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ReflectionLoaderTest {

    @Test
    public void annotationLoaderTest() throws NoSuchMethodException {
        ReflectionLoader loader = new ReflectionLoader(MiscModule.class);

        List<ModuleBuilder> modules = loader.getModules();
        List<CommandBuilder> commands = loader.getCommands(MiscModule.class);
        List<ParamBuilder> params = loader.getParams(MiscModule.class.getDeclaredMethod("repeatCommand", String.class, int.class));
        List<AdapterBuilder> adapters = loader.getAdapters();
        List<ProviderBuilder> providers = loader.getProviders();

        assertAll("Check that this loader loads all the correct number of each special class.",
            () -> assertEquals(1, modules.size()),
            () -> assertEquals(1, commands.size()),
            () -> assertEquals(2, params.size()),
            () -> assertNull(adapters),
            () -> assertNull(providers)
        );
    }

    @Test
    public void moduleDetails() {
        ReflectionLoader loader = new ReflectionLoader(MiscModule.class);

        List<ModuleBuilder> modules = loader.getModules();
        ModuleBuilder module = modules.get(0);

        assertAll("Verify ModuleBuilder has the data we're expecting.",
            () -> assertEquals(MiscModule.class, module.getHandlerType()),
            () -> assertNull(module.getGroup()),
            () -> assertEquals("Misc", module.getName()),
            () -> assertFalse(module.getAliases().isEmpty()),
            () -> assertNull(module.getHelp())
        );
    }

    @Test
    public void commandDetails() {
        ReflectionLoader loader = new ReflectionLoader(MiscModule.class);

        List<CommandBuilder> commands = loader.getCommands(MiscModule.class);
        CommandBuilder command = commands.get(0);

        assertAll("Verify CommandBuilder has the data we're expecting.",
            () -> assertEquals("Repeat", command.getName()),
            () -> assertFalse(command.getAliases().isEmpty()),
            () -> assertNull(command.getHelp())
        );
    }

    @Test
    public void paramDetails() throws NoSuchMethodException {
        ReflectionLoader loader = new ReflectionLoader(MiscModule.class);

        Method method = MiscModule.class.getDeclaredMethod("repeatCommand", String.class, int.class);
        List<ParamBuilder> params = loader.getParams(method);
        ParamBuilder param = params.get(0);

        assertAll("Verify ParamBuilder has the data we're expecting.",
            () -> assertEquals(String.class, param.getType()),
            () -> assertNotNull(param.getName()),
            () -> assertNull(param.getHelp()),
            () -> assertNull(param.getDefaultValue())
        );
    }

    @Test
    public void paramDetailsWithDefaultValue() throws NoSuchMethodException {
        ReflectionLoader loader = new ReflectionLoader(MiscModule.class);

        Method method = MiscModule.class.getDeclaredMethod("repeatCommand", String.class, int.class);
        List<ParamBuilder> params = loader.getParams(method);
        ParamBuilder param = params.get(1);

        assertAll("Verify ParamBuilder with default value has the data we're expecting.",
            () -> assertEquals(int.class, param.getType()),
            () -> assertNotNull(param.getName()),
            () -> assertNull(param.getHelp()),
            () -> assertNull(param.getDefaultValue())
        );
    }
}

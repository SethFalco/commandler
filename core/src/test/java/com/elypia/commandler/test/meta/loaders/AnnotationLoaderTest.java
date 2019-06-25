package com.elypia.commandler.test.meta.loaders;

import com.elypia.commandler.loader.AnnotationLoader;
import com.elypia.commandler.metadata.builder.*;
import com.elypia.commandler.providers.MiscToStringProvider;
import com.elypia.commandler.test.integration.impl.modules.MiscModule;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AnnotationLoaderTest {

    @Test
    public void loadSingleClass() throws NoSuchMethodException {
        AnnotationLoader loader = new AnnotationLoader(MiscModule.class);

        List<ModuleBuilder> modules = loader.getModules();
        List<CommandBuilder> commands = loader.getCommands(MiscModule.class);
        List<ParamBuilder> params = loader.getParams(MiscModule.class.getDeclaredMethod("repeatCommand", String.class, int.class));
        List<AdapterBuilder> adapters = loader.getAdapters();
        List<ProviderBuilder> providers = loader.getProviders();

        assertAll("Check that this loader loads all the correct number of each special class.",
            () -> assertEquals(1, modules.size()),
            () -> assertEquals(1, commands.size()),
            () -> assertEquals(2, params.size()),
            () -> assertTrue(adapters.isEmpty()),
            () -> assertTrue(providers.isEmpty())
        );
    }

    @Test
    public void moduleDetails() {
        AnnotationLoader loader = new AnnotationLoader(MiscModule.class);

        List<ModuleBuilder> modules = loader.getModules();
        ModuleBuilder module = modules.get(0);

        assertAll("Verify ModuleBuilder has the data we're expecting.",
            () -> assertEquals(MiscModule.class, module.getHandlerType()),
            () -> assertNull(module.getGroup()),
            () -> assertEquals("Miscellaneous", module.getName()),
            () -> assertFalse(module.getAliases().isEmpty()),
            () -> assertEquals("Test generic functionality and if it works.", module.getHelp())
        );
    }

    @Test
    public void commandDetails() {
        AnnotationLoader loader = new AnnotationLoader(MiscModule.class);

        List<CommandBuilder> commands = loader.getCommands(MiscModule.class);
        CommandBuilder command = commands.get(0);

        assertAll("Verify CommandBuilder has the data we're expecting.",
            () -> assertEquals("Repeat", command.getName()),
            () -> assertFalse(command.getAliases().isEmpty()),
            () -> assertEquals("Repeat text one or more times.", command.getHelp())
        );
    }

    @Test
    public void paramDetails() throws NoSuchMethodException {
        AnnotationLoader loader = new AnnotationLoader(MiscModule.class);

        Method method = MiscModule.class.getDeclaredMethod("repeatCommand", String.class, int.class);
        List<ParamBuilder> params = loader.getParams(method);
        ParamBuilder param = params.get(0);

        assertAll("Verify ParamBuilder has the data we're expecting.",
            () -> assertEquals(String.class, param.getType()),
            () -> assertEquals("input", param.getName()),
            () -> assertEquals("What to say?", param.getHelp()),
            () -> assertNull(param.getDefaultValue())
        );
    }

    @Test
    public void paramDetailsWithDefaultValue() throws NoSuchMethodException {
        AnnotationLoader loader = new AnnotationLoader(MiscModule.class);

        Method method = MiscModule.class.getDeclaredMethod("repeatCommand", String.class, int.class);
        List<ParamBuilder> params = loader.getParams(method);
        ParamBuilder param = params.get(1);

        assertAll("Verify ParamBuilder with default value has the data we're expecting.",
            () -> assertEquals(int.class, param.getType()),
            () -> assertEquals("count", param.getName()),
            () -> assertEquals("How many times?", param.getHelp()),
            () -> assertEquals("1", param.getDefaultValue()[0])
        );
    }

    @Test
    public void providerTest() {
        AnnotationLoader loader = new AnnotationLoader(MiscToStringProvider.class);

        List<ProviderBuilder> providers = loader.getProviders();
        ProviderBuilder provider = providers.get(0);

        assertAll("Verify ParamBuilder with default value has the data we're expecting.",
            () -> assertEquals(MiscToStringProvider.class, provider.getProviderType()),
            () -> assertEquals(String.class, provider.getBuildType()),
            () -> assertFalse(provider.getCompatibleTypes().isEmpty())
        );
    }
}

package com.elypia.commandler.loader;

import com.electronwill.nightconfig.core.conversion.ObjectConverter;
import com.electronwill.nightconfig.core.file.FileConfig;
import com.elypia.commandler.configuration.*;
import com.elypia.commandler.interfaces.*;
import com.elypia.commandler.meta.builder.*;

import java.lang.reflect.*;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The ConfigurationLoader uses NightConfig so it is only
 * compatible with the following:
 * <ul>
 *     <li>TOML</li>
 *     <li>YAML</li>
 *     <li>JSON</li>
 *     <li>HOCON</li>
 * </ul>
 *
 * And will look for the files in this order, and will
 * only accept the first file it finds.
 *
 * <strong>You must have a runtime dependency on the NightConfig
 * implementation you want to use.</strong>
 */
public class ConfigurationLoader implements MetaLoader {

    /**
     * The default path to look for files not including file types.
     */
    private static final String DEFAULT_PATH = "/commandler";

    /**
     * Commandler uses NightConfig for file configurations.
     *
     * @see <a href="https://github.com/TheElectronWill/Night-Config">NightConfig on GitHub</a>
     */
    private static final String[] FILE_TYPES = {
        ".toml",
        ".yml", ".yaml",
        ".json"
    };

    private Configuration configuration;

    public ConfigurationLoader() {
        this(DEFAULT_PATH);
    }

    public ConfigurationLoader(String path) {
        ClassLoader loader = getClass().getClassLoader();

        for (String type : FILE_TYPES) {
            URL url = loader.getResource(path + type);

            if (url == null)
                continue;

            FileConfig fileConfig = FileConfig.of(url.getPath());
            fileConfig.load();

            configuration = new ObjectConverter().toObject(fileConfig, Configuration::new);
            return;
        }
    }

    @Override
    public List<Method> findCommands(Class<? extends Handler> type) {
        for (ModuleConfig module : configuration) {
            if (module.getHandler() != type)
                continue;

            return module.getCommands().stream()
                .map(CommandConfig::getMethod)
                .collect(Collectors.toList());
        }

        return null;
    }

    @Override
    public ModuleBuilder loadModule(ModuleBuilder builder) {
        for (ModuleConfig module : configuration) {
            if (module.getHandler() != builder.getHandlerType())
                continue;

            return builder
                .setName(module.getName())
                .setGroup(module.getGroup())
                .addAliases(module.getAliases().toArray(new String[0]))
                .setHelp(module.getHelp());
        }

        return builder;
    }

    @Override
    public CommandBuilder loadCommand(CommandBuilder builder) {
        Method method = builder.getMethod();
        Class type = method.getDeclaringClass();

        for (ModuleConfig module : configuration) {
            if (module.getHandler() != type)
                continue;

            for (CommandConfig command : module) {
                if (command.getMethod() != method)
                    continue;

                return builder
                    .setName(command.getName())
                    .setAliases(command.getAliases().toArray(new String[0]))
                    .setHelp(command.getHelp())
                    .setStatic(command.isStatic())
                    .setDefault(command.isDefault());
            }
        }

        return builder;
    }

    @Override
    public ParamBuilder loadParam(ParamBuilder builder) {
        Executable method = builder.getParameter().getDeclaringExecutable();
        Class type = method.getDeclaringClass();

        for (ModuleConfig module : configuration) {
            if (module.getHandler() != type)
                continue;

            for (CommandConfig command : module) {
                if (command.getMethod() != method)
                    continue;

                for (ParamConfig param : command) {
                    return builder
                        .setName(param.getName())
                        .setHelp(param.getHelp());
                }
            }
        }

        return builder;
    }

    @Override
    public AdapterBuilder loadParser(AdapterBuilder builder) {
        return null;
    }

    @Override
    public ProviderBuilder loadBuilder(ProviderBuilder builder) {
        return null;
    }

    public Configuration getConfiguration() {
        return configuration;
    }
}

package com.elypia.commandler.interfaces;

import com.elypia.commandler.metadata.MetaBuilder;
import com.elypia.commandler.metadata.builder.*;

import java.lang.reflect.*;
import java.util.List;

public interface MetaLoader {

    /**
     * @return A list of ModuleBuilders which can be further
     * modified or merged by other loaders.
     */
    List<ModuleBuilder> getModules();

    /**
     * @param type The type of module to obtain commands for.
     * @return A list of {@link CommandBuilder} which can be further
     * modified or merged by other loaders.
     */
    List<CommandBuilder> getCommands(Class<? extends Handler> type);

    /**
     * @param method The {@link Method} that performs a {@link CommandBuilder command}
     *               which may or may not take {@link Parameter parameters.}.
     * @return A list of {@link CommandBuilder} which can be further modified or
     * {@link MetaBuilder#merge(MetaBuilder) merged} by other {@link MetaLoader loaders}.
     */
    List<ParamBuilder> getParams(Method method);
    List<AdapterBuilder> getAdapters();
    List<ProviderBuilder> getProviders();
}

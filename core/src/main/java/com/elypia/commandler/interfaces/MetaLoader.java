package com.elypia.commandler.interfaces;

import com.elypia.commandler.meta.builder.*;

import java.lang.reflect.Method;
import java.util.List;

public interface MetaLoader {
    List<ModuleBuilder> getModules();
    List<CommandBuilder> getCommands(Class<? extends Handler> type);
    List<ParamBuilder> getParams(Method method);
    List<AdapterBuilder> getAdapters();
    List<ProviderBuilder> getProviders();
}

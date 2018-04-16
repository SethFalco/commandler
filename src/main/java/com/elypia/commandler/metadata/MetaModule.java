package com.elypia.commandler.metadata;

import com.elypia.commandler.CommandHandler;
import com.elypia.commandler.annotations.command.Module;

public class MetaModule {

    private Class<? extends CommandHandler> clazz;
    private Module module;
    private MetaCommand commands;

    public <T extends CommandHandler> MetaModule(T t) {
        clazz = t.getClass();
        module = clazz.getAnnotation(Module.class);

    }

    public Class<? extends CommandHandler> getHandler() {
        return clazz;
    }

    public Module getModule() {
        return module;
    }
}

package com.elypia.commandler.metadata;

import com.elypia.commandler.CommandHandler;
import com.elypia.commandler.annotations.*;

import java.lang.reflect.Method;
import java.util.*;

public class MetaModule {

    private Class<? extends CommandHandler> clazz;
    private Module module;
    private Collection<MetaCommand> commands;

    public static <T extends CommandHandler> MetaModule of(T t) {
        return new MetaModule(t);
    }

    private <T extends CommandHandler> MetaModule(T t) {
        commands = new ArrayList<>();
        clazz = t.getClass();
        module = clazz.getAnnotation(Module.class);

        Method[] methods = clazz.getDeclaredMethods();

        for (Method method : methods) {
            if (method.isAnnotationPresent(Command.class))
                commands.add(MetaCommand.of(method));
        }
    }

    public Class<? extends CommandHandler> getHandler() {
        return clazz;
    }

    public Module getModule() {
        return module;
    }

    public Collection<MetaCommand> getCommands() {
        return commands;
    }
}

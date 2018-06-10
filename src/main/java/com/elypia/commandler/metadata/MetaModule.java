package com.elypia.commandler.metadata;

import com.elypia.commandler.Commandler;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.modules.CommandHandler;

import java.lang.reflect.Method;
import java.util.*;

public class MetaModule {

    private Commandler commandler;
    private Class<? extends CommandHandler> clazz;
    private Module module;
    private Collection<MetaCommand> commands;

    public static <T extends CommandHandler> MetaModule of(Commandler commandler, T t) {
        return new MetaModule(commandler, t);
    }

    private <T extends CommandHandler> MetaModule(Commandler commandler, T t) {
        this.commandler = commandler;
        commands = new ArrayList<>();
        clazz = t.getClass();
        module = clazz.getAnnotation(Module.class);

        Method[] methods = clazz.getDeclaredMethods();

        for (Method method : methods) {
            if (method.isAnnotationPresent(Command.class))
                commands.add(MetaCommand.of(commandler, method));
        }
    }

    public Commandler getCommandler() {
        return commandler;
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

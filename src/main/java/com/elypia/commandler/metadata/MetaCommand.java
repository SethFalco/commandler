package com.elypia.commandler.metadata;

import com.elypia.commandler.annotations.*;
import com.elypia.commandler.events.MessageEvent;

import java.lang.reflect.*;
import java.util.*;

public class MetaCommand {

    private Method method;
    private Module module;
    private Command command;
    private List<MetaParam> params;
    private boolean isStatic;
    private boolean isDefault;

    public static MetaCommand of(Method method) {
        return new MetaCommand(method);
    }

    private MetaCommand(Method method) {
        this.method = method;
        params = new ArrayList<>();

        Class<?> clazz = method.getDeclaringClass();
        module = clazz.getAnnotation(Module.class);
        command = method.getAnnotation(Command.class);
        isStatic = method.isAnnotationPresent(Static.class);
        isDefault = method.isAnnotationPresent(Default.class);

        Param[] params = method.getAnnotationsByType(Param.class);
        Parameter[] parameters = method.getParameters();
        int offset = 0;

        for (int i = 0; i < parameters.length; i++) {
            if (parameters[i].getType() == MessageEvent.class) {
                offset++;

                MetaParam meta = MetaParam.of(parameters[i], null);
                this.params.add(meta);
                continue;
            }

            MetaParam meta = MetaParam.of(parameters[i], params[i - offset]);
            this.params.add(meta);
        }
    }

    public Module getModule() {
        return module;
    }

    public Command getCommand() {
        return command;
    }

    public List<MetaParam> getParams() {
        return params;
    }

    public boolean isStatic() {
        return isStatic;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public Method getMethod() {
        return method;
    }
}

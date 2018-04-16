package com.elypia.commandler.metadata;

import com.elypia.commandler.annotations.command.*;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

public class MetaCommand {

    private Method method;
    private Module module;
    private Command command;
    private List<MetaParam> params;
    private boolean isStatic;
    private boolean isDefault;

    public MetaCommand(Method method) {
        this.method = method;
        params = new ArrayList<>();

        Class<?> clazz = method.getDeclaringClass();
        module = clazz.getAnnotation(Module.class);
        command = method.getAnnotation(Command.class);
        isStatic = method.isAnnotationPresent(Static.class);
        isDefault = method.isAnnotationPresent(Default.class);

        Param[] params = method.getAnnotationsByType(Param.class);
        Parameter[] parameters = method.getParameters();

        for (int i = 1; i < parameters.length; i++) {
            MetaParam meta = new MetaParam(parameters[i], params[i - 1]);
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

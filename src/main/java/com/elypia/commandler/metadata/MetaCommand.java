package com.elypia.commandler.metadata;

import com.elypia.commandler.annotations.*;
import com.elypia.commandler.events.MessageEvent;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;

public class MetaCommand {

    private Class<?> clazz;
    private Method method;
    private Map<Class<?>, Annotation> annotations;
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
        annotations = new HashMap<>();
        clazz = method.getDeclaringClass();

        for (Annotation annotation : method.getDeclaredAnnotations())
            annotations.put(annotation.annotationType(), annotation);

        for (Annotation annotation : clazz.getDeclaredAnnotations()) {
            if (!annotations.containsKey(annotation.annotationType()))
                annotations.put(annotation.annotationType(), annotation);
        }

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

    public Map<Class<?>, Annotation> getAnnotations() {
        return annotations;
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

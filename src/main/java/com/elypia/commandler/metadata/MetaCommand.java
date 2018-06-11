package com.elypia.commandler.metadata;

import com.elypia.commandler.Commandler;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.events.MessageEvent;
import com.elypia.commandler.validation.ICommandValidator;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;

public class MetaCommand {

    private Commandler commandler;
    private Class<?> clazz;
    private Method method;
    private Map<Class<?>, Annotation> annotations;
    private Module module;
    private Command command;
    private List<MetaParam> params;
    private boolean isStatic;
    private boolean isDefault;

    public static MetaCommand of(Commandler commandler, Method method) {
        return new MetaCommand(commandler, method);
    }

    private MetaCommand(Commandler commandler, Method method) {
        this.commandler = commandler;
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

    public Commandler getCommandler() {
        return commandler;
    }

    public Module getModule() {
        return module;
    }

    public Map<Class<?>, Annotation> getAnnotations() {
        return annotations;
    }

    public Map<Annotation, ICommandValidator> getValidators() {
        Map<Class<? extends Annotation>, ICommandValidator> registeredValidators = commandler.getDispatcher().getValidator().getCommandValidators();
        Map<Annotation, ICommandValidator> validators = new HashMap<>();

        annotations.forEach((clazz, annotation) -> {
            registeredValidators.forEach((clazz2, validator)-> {
                if (clazz == clazz2)
                    validators.put(annotation, validator);
            });
        });

        return validators;
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

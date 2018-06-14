package com.elypia.commandler.metadata;

import com.elypia.commandler.Commandler;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.events.MessageEvent;
import com.elypia.commandler.exceptions.RecursiveAliasException;
import com.elypia.commandler.modules.CommandHandler;
import com.elypia.commandler.validation.ICommandValidator;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;

public class MetaCommand {

    private Commandler commandler;
    private CommandHandler handler;
    private Class<? extends CommandHandler> clazz;
    private MetaModule metaModule;
    private Command command;
    private Method method;
    private Map<Class<?>, Annotation> annotations;
    private List<MetaParam> metaParams;
    private Collection<String> aliases;
    private boolean isStatic;
    private boolean isDefault;
    private boolean isPublic;

    public static MetaCommand of(MetaModule module, Method method) {
        return new MetaCommand(module, method);
    }

    private MetaCommand(MetaModule metaModule, Method method) {
        this.metaModule = Objects.requireNonNull(metaModule);
        this.method = Objects.requireNonNull(method);

        clazz = metaModule.getHandlerType();
        command = method.getAnnotation(Command.class);
        aliases = new ArrayList<>();

        for (String alias : command.aliases())
            aliases.add(alias.toLowerCase());

        if (aliases.stream().distinct().count() != aliases.size()) {
            String name = command.name();
            String moduleName = metaModule.getModule().name();
            String format = "Command %s in module %s (%s) contains multiple aliases which are identical.";
            throw new RecursiveAliasException(String.format(format, name, moduleName, clazz.getName()));
        }

        metaParams = new ArrayList<>();
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

        annotations = new HashMap<>();

        for (Annotation annotation : method.getDeclaredAnnotations())
            annotations.put(annotation.annotationType(), annotation);

        for (Annotation annotation : clazz.getDeclaredAnnotations()) {
            if (!annotations.containsKey(annotation.annotationType()))
                annotations.put(annotation.annotationType(), annotation);
        }

        isStatic = annotations.remove(Static.class) != null;
        isDefault = annotations.remove(Default.class) != null;

        commandler = metaModule.getCommandler();
        handler = metaModule.getHandler();
        isPublic = command.help().equals("");
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

    public Collection<String> getAliases() {
        return aliases;
    }

    public Method getMethod() {
        return method;
    }
}

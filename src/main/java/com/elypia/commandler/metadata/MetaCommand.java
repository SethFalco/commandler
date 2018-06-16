package com.elypia.commandler.metadata;

import com.elypia.commandler.Commandler;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.events.MessageEvent;
import com.elypia.commandler.exceptions.MalformedCommandException;
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
    private Map<Class<? extends Annotation>, Annotation> annotations;
    private Collection<MetaValidator> validators;
    private Set<String> aliases;
    private boolean isStatic;
    private boolean isDefault;
    private boolean isPublic;
    private List<MetaParam> metaParams;

    public static MetaCommand of(MetaModule module, Method method) {
        return new MetaCommand(module, method);
    }

    private MetaCommand(MetaModule metaModule, Method method) {
        this.metaModule = Objects.requireNonNull(metaModule);
        this.method = Objects.requireNonNull(method);

        clazz = metaModule.getHandlerType();
        command = method.getAnnotation(Command.class);
        aliases = new HashSet<>();

        for (String alias : command.aliases())
            aliases.add(alias.toLowerCase());

        if (aliases.size() != command.aliases().length) {
            String format = "Command %s in module %s (%s) contains multiple aliases which are identical.";
            System.err.printf(format, command.name(), metaModule.getModule().name(), clazz.getName());
        }

        metaParams = new ArrayList<>();
        Param[] params = method.getAnnotationsByType(Param.class);
        Parameter[] parameters = method.getParameters();

        if (Arrays.stream(parameters).filter(o -> o.getType() != MessageEvent.class).count() != params.length) {
            String format = "Command %s in module %s (%s) doesn't contain the correct number of @Param annotations.";
            String message = String.format(format, command.name(), metaModule.getModule().name(), clazz.getName());
            throw new MalformedCommandException(message);
        }

        int offset = 0;

        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            Param param = null;

            if (parameter.getType() != MessageEvent.class)
                param = params[i - offset];

            else if (++offset == 2)
                System.err.printf("Command %s in module %s (%s) contains multiple MessageEvent parameters, there is no benefit to this.", command.name(), metaModule.getModule().name(), clazz.getName());

            MetaParam meta = MetaParam.of(parameter, param);
            metaParams.add(meta);
        }

        validators = new ArrayList<>();
        annotations = parseAnnotations(method, clazz);

        commandler = metaModule.getCommandler();
        handler = metaModule.getHandler();
        isPublic = !command.help().equals("");
    }

    private Map<Class<? extends Annotation>, Annotation> parseAnnotations(Method method, Class<? extends CommandHandler> clazz) {
        Map<Class<? extends Annotation>, Annotation> annotations = new HashMap<>();

        for (Annotation annotation : method.getDeclaredAnnotations()) {
            Class<? extends Annotation> type = annotation.annotationType();

            if (type.isAnnotationPresent(Validator.class))
                validators.add(MetaValidator.of(this, annotation));
            else if (type == Static.class)
                isStatic = true;
            else if (type == Default.class)
                isDefault = true;
            else
                annotations.put(type, annotation);
        }

        for (Annotation annotation : clazz.getDeclaredAnnotations()) {
            if (!annotations.containsKey(annotation.annotationType()))
                annotations.put(annotation.annotationType(), annotation);
        }

        return annotations;
    }

    public Commandler getCommandler() {
        return commandler;
    }

    public CommandHandler getHandler() {
        return handler;
    }

    public Class<? extends CommandHandler> getHandlerType() {
        return clazz;
    }

    public MetaModule getMetaModule() {
        return metaModule;
    }

    public Command getCommand() {
        return command;
    }

    public Method getMethod() {
        return method;
    }

    public Map<Class<? extends Annotation>, Annotation> getAnnotations() {
        return annotations;
    }

    public Map<MetaValidator, ICommandValidator> getValidators() {
        Map<MetaValidator, ICommandValidator> validatorMap = new HashMap<>();

        commandler.getDispatcher().getValidator().getCommandValidators().forEach((type, validator) -> {
            validators.forEach(metaCommandValidator -> {
                if (metaCommandValidator.getAnnotationType() == type)
                    validatorMap.put(metaCommandValidator, validator);
            });
        });

        return validatorMap;
    }

    public Set<String> getAliases() {
        return aliases;
    }

    public boolean isStatic() {
        return isStatic;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public List<MetaParam> getMetaParams() {
        return metaParams;
    }
}

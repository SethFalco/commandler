package com.elypia.commandler.metadata.loader;

import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.interfaces.*;
import com.elypia.commandler.metadata.builder.*;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class AnnotationLoader implements MetadataLoader {

    @Override
    public List<Method> findCommands(Class<? extends Handler> clazz) {
        return Arrays.stream(clazz.getMethods())
            .filter((m) -> m.isAnnotationPresent(Command.class))
            .collect(Collectors.toList());
    }

    @Override
    public ModuleBuilder loadModule(ModuleBuilder builder) {
        Module module = builder.getHandlerClass().getAnnotation(Module.class);

        if (module == null)
            return builder;

        return builder
            .setName(module.name())
            .setGroup(module.group())
            .addAliases(module.aliases())
            .setHelp(module.help())
            .setHidden(module.hidden());
    }

    @Override
    public CommandBuilder loadCommand(CommandBuilder builder) {
        Method method = builder.getMethod();
        Command command = method.getAnnotation(Command.class);

        if (command == null)
            return builder;

        return builder
            .setName(command.name())
            .setAliases(command.aliases())
            .setHelp(command.help())
            .setStatic(method.isAnnotationPresent(Static.class))
            .setDefault(method.isAnnotationPresent(Default.class));
    }

    @Override
    public ParamBuilder loadParam(ParamBuilder builder) {
        Param param = builder.getParameter().getAnnotation(Param.class);

        if (param == null)
            return builder;

        return builder
            .setName(param.name())
            .setHelp(param.help());
    }

    @Override
    public AdapterBuilder loadParser(AdapterBuilder builder) {
        Class<? extends Adapter> clazz = builder.getParserClass();

        if (!clazz.isAnnotationPresent(Compatible.class))
            return builder;

        Compatible compatible = clazz.getAnnotation(Compatible.class);
        builder.setCompatible(List.of(compatible.value()));

        return builder;
    }

    @Override
    public ProviderBuilder loadBuilder(ProviderBuilder builder) {
        Class<? extends ResponseProvider> clazz = builder.getBuilderClass();

        if (!clazz.isAnnotationPresent(Compatible.class))
            return builder;

        Compatible compatible = clazz.getAnnotation(Compatible.class);
        builder.setCompatible(List.of(compatible.value()));

        return builder;
    }
}

package com.elypia.commandler.meta.loaders;

import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.interfaces.*;
import com.elypia.commandler.meta.builder.*;
import com.elypia.commandler.utils.AnnoUtils;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.*;

public class AnnotationLoader extends SearchingLoader {

    public AnnotationLoader(Class<?> reference) {
        super(reference);
    }

    public AnnotationLoader(Package pkge) {
        super(pkge);
    }

    public AnnotationLoader(String pkge) throws IOException {
        super(pkge);
    }

    @Override
    public List<ModuleBuilder> getModules() {
        return types.stream()
            .filter((t) -> t.isAssignableFrom(Handler.class) && t.isAnnotationPresent(Module.class))
            .map((t) -> {
                Module module = t.getDeclaredAnnotation(Module.class);
                return new ModuleBuilder((Class<? extends Handler>)t)
                    .setGroup(AnnoUtils.effectivelyNullElse(module.group()))
                    .setName(AnnoUtils.effectivelyNullElse(module.name()))
                    .setAliases(AnnoUtils.effectivelyNullElse(module.aliases()))
                    .setHelp(AnnoUtils.effectivelyNullElse(module.help()))
                    .setHidden(module.hidden());
            })
            .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public List<CommandBuilder> getCommands(Class<? extends Handler> type) {
        return Stream.of(type.getDeclaredMethods())
            .filter((m) -> m.isAnnotationPresent(Command.class))
            .map((m) -> {
                Command command = m.getDeclaredAnnotation(Command.class);
                return new CommandBuilder(m)
                    .setName(AnnoUtils.effectivelyNullElse(command.name()))
                    .setAliases(AnnoUtils.effectivelyNullElse(command.aliases()))
                    .setHelp(AnnoUtils.effectivelyNullElse(command.help()))
                    .setHidden(command.hidden())
                    .setDefault(m.isAnnotationPresent(Default.class))
                    .setStatic(m.isAnnotationPresent(Static.class));
            })
            .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public List<ParamBuilder> getParams(Method method) {
        return Stream.of(method.getParameters())
            .filter((t) -> t.isAnnotationPresent(Param.class))
            .map((p) -> {
                Param param = p.getDeclaredAnnotation(Param.class);
                return new ParamBuilder(p.getType())
                    .setName(AnnoUtils.effectivelyNullElse(param.name()))
                    .setHelp(AnnoUtils.effectivelyNullElse(param.help()))
                    .setDefaultValue(AnnoUtils.effectivelyNullElse(param.defaultValue()));
            })
            .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public List<AdapterBuilder> getAdapters() {
        return types.stream()
            .filter((t) -> t.isAssignableFrom(Adapter.class) && t.isAnnotationPresent(Compatible.class))
            .map((a) -> {
                Compatible compatible = a.getDeclaredAnnotation(Compatible.class);
                return new AdapterBuilder((Class<? extends Adapter>) a)
                    .setCompatibleTypes(compatible.value());
            })
            .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public List<ProviderBuilder> getProviders() {
        return types.stream()
            .filter((t) -> t.isAssignableFrom(ResponseProvider.class) && t.isAnnotationPresent(Compatible.class))
            .map((a) -> {
                Compatible compatible = a.getDeclaredAnnotation(Compatible.class);
                return new ProviderBuilder((Class<? extends ResponseProvider<?, ?>>) a)
                    .setCompatibleTypes(compatible.value());
            })
            .collect(Collectors.toUnmodifiableList());
    }
}

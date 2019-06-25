package com.elypia.commandler.loader;

import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.interfaces.*;
import com.elypia.commandler.metadata.builder.*;
import com.elypia.commandler.utils.AnnoUtils;
import org.slf4j.*;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.*;

public class AnnotationLoader extends SearchingLoader {

    private static final Logger logger = LoggerFactory.getLogger(AnnotationLoader.class);

    public AnnotationLoader(Class<?>... types) {
        super(types);
    }

    public AnnotationLoader(Package pkge) {
        super(pkge);
    }

    public AnnotationLoader(String pkge) throws IOException {
        super(pkge);
    }

    @Override
    public List<ModuleBuilder> getModules() {
        logger.debug("Getting Modules for {}.", this);

        return types.stream()
            .filter((t) -> {
                boolean isHandler = Handler.class.isAssignableFrom(t);
                boolean isModule = t.isAnnotationPresent(Module.class);
                logger.debug("Checking if {} is module. (Handler: {} | Module: {})", t.getSimpleName(), isHandler, isModule);

                return isHandler && isModule;
            })
            .map((t) -> {
                Module module = t.getDeclaredAnnotation(Module.class);
                return new ModuleBuilder((Class<? extends Handler>)t)
                    .setGroup(AnnoUtils.ifEffectivelyNull(module.group()))
                    .setName(AnnoUtils.ifEffectivelyNull(module.name()))
                    .setAliases(AnnoUtils.ifEffectivelyNull(module.aliases()))
                    .setHelp(AnnoUtils.ifEffectivelyNull(module.help()))
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
                    .setName(AnnoUtils.ifEffectivelyNull(command.name()))
                    .setAliases(AnnoUtils.ifEffectivelyNull(command.aliases()))
                    .setHelp(AnnoUtils.ifEffectivelyNull(command.help()))
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
                    .setName(AnnoUtils.ifEffectivelyNull(param.name()))
                    .setHelp(AnnoUtils.ifEffectivelyNull(param.help()))
                    .setDefaultValue(AnnoUtils.ifEffectivelyNull(param.defaultValue()));
            })
            .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public List<AdapterBuilder> getAdapters() {
        return types.stream()
            .filter((t) -> ParamAdapter.class.isAssignableFrom(t) && t.isAnnotationPresent(Adapter.class))
            .map((a) -> {
                Adapter adapter = a.getDeclaredAnnotation(Adapter.class);
                return new AdapterBuilder((Class<? extends ParamAdapter>) a)
                    .setCompatibleTypes(adapter.value());
            })
            .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public List<ProviderBuilder> getProviders() {
        return types.stream()
            .filter((t) -> ResponseProvider.class.isAssignableFrom(t) && t.isAnnotationPresent(Provider.class))
            .map((a) -> {
                Provider provider = a.getDeclaredAnnotation(Provider.class);
                return new ProviderBuilder((Class<? extends ResponseProvider<?, ?>>) a)
                    .setBuildType(provider.provides())
                    .setCompatibleTypes(provider.value());
            })
            .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public String toString() {
        return String.format("%s (%,d Types)", getClass().getSimpleName(), types.size());
    }
}

package com.elypia.commandler.metadata.loader;

import com.elypia.commandler.Handler;
import com.elypia.commandler.interfaces.CommandlerEvent;
import com.elypia.commandler.metadata.builder.*;

import java.lang.reflect.*;
import java.util.List;
import java.util.stream.*;

public interface MetadataLoader {

    List<Method> findCommands(Class<? extends Handler> clazz);

    default List<Parameter> findParams(Method method) {
        return Stream.of(method.getParameters())
            .filter((p) -> !CommandlerEvent.class.isAssignableFrom(p.getType()))
            .collect(Collectors.toList());
    }

    ModuleBuilder loadModule(ModuleBuilder builder);
    CommandBuilder loadCommand(CommandBuilder builder);
    ParamBuilder loadParam(ParamBuilder builder);

    ParserBuilder loadParser(ParserBuilder builder);
    BuilderBuilder loadBuilder(BuilderBuilder builder);
}

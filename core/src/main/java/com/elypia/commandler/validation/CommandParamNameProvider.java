package com.elypia.commandler.validation;

import com.elypia.commandler.Context;
import com.elypia.commandler.interfaces.Handler;
import com.elypia.commandler.metadata.data.*;

import javax.validation.ParameterNameProvider;
import java.lang.reflect.*;
import java.util.*;
import java.util.stream.Collectors;

public class CommandParamNameProvider implements ParameterNameProvider {

    private Context context;

    public CommandParamNameProvider(Context context) {
        this.context = Objects.requireNonNull(context);
    }

    @Override
    public List<String> getParameterNames(Constructor<?> constructor) {
        return getJavaNames(constructor);
    }

    @Override
    public List<String> getParameterNames(Method method) {
        Class<?> type = method.getDeclaringClass();
        MetaModule module = context.getModule((Class<? extends Handler>)type);

        if (module == null)
            return getJavaNames(method);

        List<MetaParam> params = null;

        for (MetaCommand command : module.getCommands()) {
            if (method.equals(command.getMethod())) {
                params = command.getParams();
                break;
            }
        }

        List<String> names = params.stream()
            .map(MetaParam::getName)
            .collect(Collectors.toList());

        while (names.size() < method.getParameterCount())
            names.add("");

        return names;
    }

    private List<String> getJavaNames(Executable executable) {
        List<String> params = new ArrayList<>();

        for (Parameter parameter : executable.getParameters())
            params.add(parameter.getName());

        return params;
    }
}

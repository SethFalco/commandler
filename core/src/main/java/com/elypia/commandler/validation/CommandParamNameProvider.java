package com.elypia.commandler.validation;

import com.elypia.commandler.Handler;
import com.elypia.commandler.metadata.Context;
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
        Class<?> clazz = method.getDeclaringClass();
        MetaModule module = context.getModule((Class<Handler>)clazz);

        if (module == null)
            return getJavaNames(method);

        List<ParamData> params = null;

        outer:
        for (MetaCommand command : module.getCommands()) {
            if (method.equals(command.getMethod())) {
                params = command.getDefaultParams();
                break;
            }

            for (OverloadData overload : command.getOverloads()) {
                if (overload.getMethod().equals(method)) {
                    params = overload.getParams();
                    break outer;
                }
            }
        }

        List<String> names = params.stream()
            .map(ParamData::getName)
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

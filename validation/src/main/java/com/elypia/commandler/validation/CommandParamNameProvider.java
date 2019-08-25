package com.elypia.commandler.validation;

import com.elypia.commandler.Context;
import com.elypia.commandler.api.Controller;
import com.elypia.commandler.metadata.*;

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
        MetaController module = context.getModule((Class<? extends Controller>)type);

        if (module == null)
            return getJavaNames(method);

        List<MetaParam> metaParams = null;

        for (MetaCommand metaCommand : module.getMetaCommands()) {
            if (method.equals(metaCommand.getMethod())) {
                metaParams = metaCommand.getMetaParams();
                break;
            }
        }

        List<String> names = metaParams.stream()
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

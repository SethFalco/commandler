package com.elypia.commandler.validation;

import com.elypia.commandler.Handler;
import com.elypia.commandler.metadata.Context;
import com.elypia.commandler.metadata.data.*;

import javax.validation.ParameterNameProvider;
import java.lang.reflect.*;
import java.util.*;

public class CommandParamNameProvider implements ParameterNameProvider {

    private Context context;

    public CommandParamNameProvider(Context context) {
        this.context = context;
    }

    @Override
    public List<String> getParameterNames(Constructor<?> constructor) {
        return getJavaNames(constructor);
    }

    @Override
    public List<String> getParameterNames(Method method) {
        Class<?> clazz = method.getDeclaringClass();
        ModuleData module = context.getModule((Class<Handler>)clazz);

        if (module == null)
            return getJavaNames(method);

        for (CommandData command : module.getCommands()) {
            for (CommandData overload : command.getOverloads()) {
                if (overload.getMethod().equals(method)) {
                    List<String> params = new ArrayList<>();

                    for (ParamData param : overload.getParamData())
                        params.add(param.getName());

                    return params;
                }
            }
        }

        String format = "Method %s is not a command.";
        throw new IllegalStateException(String.format(format, method.getName()));
    }

    private List<String> getJavaNames(Executable executable) {
        List<String> params = new ArrayList<>();

        for (Parameter parameter : executable.getParameters())
            params.add(parameter.getName());

        return params;
    }
}

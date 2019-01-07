package com.elypia.commandler.validation;

import com.elypia.commandler.*;
import com.elypia.commandler.metadata.*;

import javax.validation.ParameterNameProvider;
import java.lang.reflect.*;
import java.util.*;

public class CommandParamNameProvider implements ParameterNameProvider {

    private ModulesContext context;

    public CommandParamNameProvider(ModulesContext context) {
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

        // If it's not a module, just return Java's names.
        if (module == null)
            return getJavaNames(method);

        // If it is a module, use our @Param annotations to return names.
        for (CommandData command : module.getCommands()) {
            for (CommandData overload : command.getOverloads()) {
                if (overload.getMethod().equals(method)) {
                    List<String> params = new ArrayList<>();

                    for (ParamData param : overload.getParamData())
                        params.add((param.isInput()) ? param.getAnnotation().id() : null);

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

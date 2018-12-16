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
        throw new IllegalStateException("Constructor is not a command.");
    }

    @Override
    public List<String> getParameterNames(Method method) {
        List<String> params = new ArrayList<>();
        ModuleData module = context.getModule((Class<Handler>)method.getDeclaringClass());

        if (module == null) {
            for (int i = 0; i < method.getParameterCount(); i++)
                params.add(null);

            return params;
        }

        for (CommandData command : module.getCommands()) {
            for (CommandData overload : command.getOverloads()) {
                if (overload.getMethod().equals(method)) {
                    for (ParamData param : overload.getParamData())
                        params.add((param.isInput()) ? param.getAnnotation().id() : null);

                    break;
                }
            }
        }

        return params;
    }
}

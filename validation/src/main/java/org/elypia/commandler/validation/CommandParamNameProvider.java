/*
 * Copyright 2019-2019 Elypia CIC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.elypia.commandler.validation;

import org.elypia.commandler.Context;
import org.elypia.commandler.api.Controller;
import org.elypia.commandler.metadata.*;

import javax.validation.ParameterNameProvider;
import java.lang.reflect.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author seth@elypia.org (Syed Shah)
 */
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

/*
 * Copyright 2019-2025 Seth Falco and Commandler Contributors
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

package fun.falco.commandler.validation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.validation.ParameterNameProvider;

import org.apache.deltaspike.core.api.provider.BeanProvider;
import fun.falco.commandler.Commandler;
import fun.falco.commandler.CommandlerExtension;
import fun.falco.commandler.i18n.CommandlerMessageResolver;
import fun.falco.commandler.metadata.MetaCommand;
import fun.falco.commandler.metadata.MetaController;
import fun.falco.commandler.metadata.MetaParam;

/**
 * Overrides how the validation implementation obtains names
 * for validation error messages.
 *
 * This ensures that invalidated parameters use their
 * {@link Commandler} {@link MetaParam} names.
 *
 * @author seth@falco.fun (Seth Falco)
 */
public class CommandParameterNameProvider implements ParameterNameProvider {

    /** Metadata for all {@link MetaController}s, {@link MetaCommand}s, and {@link MetaParam}s. */
    private final CommandlerExtension commandlerExtension;

    public CommandParameterNameProvider(final CommandlerExtension commandlerExtension) {
        this.commandlerExtension = Objects.requireNonNull(commandlerExtension);
    }

    @Override
    public List<String> getParameterNames(Constructor<?> constructor) {
        return getJavaNames(constructor);
    }

    @Override
    public List<String> getParameterNames(Method method) {
        Class<?> type = method.getDeclaringClass();

        Collection<MetaController> metaControllers = commandlerExtension.getMetaControllers();

        List<MetaController> filtered = metaControllers.stream()
           .filter((metaController) -> metaController.getControllerType() == type)
           .collect(Collectors.toList());

        if (filtered.isEmpty()) {
            return getJavaNames(method);
        } else if (filtered.size() > 1) {
            throw new IllegalStateException("Found more than 1 MetaController for the same Controller implementation.");
        }

        MetaController module = filtered.get(0);
        MetaCommand command = null;

        for (MetaCommand metaCommand : module.getMetaCommands()) {
            if (method.equals(metaCommand.getMethod())) {
                command = metaCommand;
                break;
            }
        }

        if (command == null) {
            return getJavaNames(method);
        }

        Parameter[] parameters = command.getMethod().getParameters();
        List<String> names = Stream.of(parameters).map((p) -> "").collect(Collectors.toList());
        List<MetaParam> metaParams = command.getMetaParams();

        CommandlerMessageResolver commandlerMessageResolver = BeanProvider.getContextualReference(CommandlerMessageResolver.class);

        for (MetaParam metaParam : metaParams) {
            names.set(metaParam.getCommandIndex(), commandlerMessageResolver.getMessage(metaParam.getName()));
        }

        return names;
    }

    private List<String> getJavaNames(Executable executable) {
        List<String> params = new ArrayList<>();

        for (Parameter parameter : executable.getParameters()) {
            params.add(parameter.getName());
        }

        return params;
    }
}

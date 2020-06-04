/*
 * Copyright 2019-2020 Elypia CIC
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

import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.elypia.commandler.*;
import org.elypia.commandler.api.Controller;
import org.elypia.commandler.i18n.CommandlerMessageResolver;
import org.elypia.commandler.metadata.*;
import org.slf4j.*;

import javax.validation.ParameterNameProvider;
import java.lang.reflect.*;
import java.util.*;
import java.util.stream.*;

/**
 * Overrides how the validation implementation obtains names
 * for validation error messages.
 *
 * This ensures that invalidated parameters use their
 * {@link Commandler} {@link MetaParam} names.
 *
 * @author seth@elypia.org (Seth Falco)
 */
public class CommandParamNameProvider implements ParameterNameProvider {

    /** Logging with SLF4J */
    private static final Logger logger = LoggerFactory.getLogger(CommandParamNameProvider.class);

    /** Metadata for all {@link MetaController}s, {@link MetaCommand}s, and {@link MetaParam}s. */
    private final CommandlerExtension commandlerExtension;

    public CommandParamNameProvider(final CommandlerExtension commandlerExtension) {
        this.commandlerExtension = Objects.requireNonNull(commandlerExtension);
    }

    @Override
    public List<String> getParameterNames(Constructor<?> constructor) {
        return getJavaNames(constructor);
    }

    @Override
    public List<String> getParameterNames(Method method) {
        Class<?> type = method.getDeclaringClass();

        if (!(Controller.class.isAssignableFrom(type)))
            return getJavaNames(method);

        Collection<MetaController> metaControllers = commandlerExtension.getMetaControllers();

        List<MetaController> filtered = metaControllers.stream()
           .filter((metaController) -> metaController.getControllerType() == type)
           .collect(Collectors.toList());

        if (filtered.isEmpty())
            throw new IllegalStateException("Attempted to validate Controller which wasn't added to Commandler on initialization.");
        else if (filtered.size() > 1)
            throw new IllegalStateException("Found more than 1 MetaController for the same Controller implementation.");

        MetaController module = filtered.get(0);

        MetaCommand command = null;

        for (MetaCommand metaCommand : module.getMetaCommands()) {
            if (method.equals(metaCommand.getMethod())) {
                command = metaCommand;
                break;
            }
        }

        if (command == null)
            return getJavaNames(method);

        Parameter[] parameters = command.getMethod().getParameters();
        List<String> names = Stream.of(parameters).map((p) -> "").collect(Collectors.toList());
        List<MetaParam> metaParams =  command.getMetaParams();

        CommandlerMessageResolver commandlerMessageResolver = BeanProvider.getContextualReference(CommandlerMessageResolver.class);

        for (MetaParam metaParam : metaParams)
            names.set(metaParam.getCommandIndex(), commandlerMessageResolver.getMessage(metaParam.getName()));

        return names;
    }

    private List<String> getJavaNames(Executable executable) {
        List<String> params = new ArrayList<>();

        for (Parameter parameter : executable.getParameters())
            params.add(parameter.getName());

        return params;
    }
}

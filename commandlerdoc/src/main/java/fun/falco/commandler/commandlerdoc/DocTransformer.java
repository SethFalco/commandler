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

package fun.falco.commandler.commandlerdoc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.deltaspike.core.api.provider.BeanProvider;
import fun.falco.commandler.commandlerdoc.models.ExportableCommand;
import fun.falco.commandler.commandlerdoc.models.ExportableController;
import fun.falco.commandler.commandlerdoc.models.ExportableData;
import fun.falco.commandler.commandlerdoc.models.ExportableParameter;
import fun.falco.commandler.i18n.CommandlerMessageResolver;
import fun.falco.commandler.metadata.MetaController;

/**
 * Transforms the standard metadata objects such as {@link MetaController}
 * into more appropriate exportable objects.
 *
 * @author seth@falco.fun (Seth Falco)
 * @since 4.0.2
 */
public class DocTransformer {

    public DocTransformer() {

    }

    public ExportableData transform(List<MetaController> metaControlllers, List<Locale> locales) {
        CommandlerMessageResolver resolver = BeanProvider.getContextualReference(CommandlerMessageResolver.class, false);

        ExportableData data = new ExportableData();
        Map<Locale, List<ExportableController>> exportableControllers = new HashMap<>();

        for (Locale locale : locales) {
            for (MetaController metaController : metaControlllers) {
                ExportableController exportableController = new ExportableController();
                exportableController.setLocale(locale);
                exportableController.setName(resolver.getMessage(metaController.getName(), locale));
                exportableController.setDescription(resolver.getMessage(metaController.getDescription(), locale));
                exportableController.setGroup(resolver.getMessage(metaController.getGroup(), locale));

                List<ExportableCommand> exportableCommands = metaController.getMetaCommands().stream()
                    .map((metaCommand) -> {
                        ExportableCommand exportableCommand = new ExportableCommand();
                        exportableCommand.setLocale(locale);
                        exportableCommand.setName(resolver.getMessage(metaCommand.getName(), locale));
                        exportableCommand.setDescription(resolver.getMessage(metaCommand.getDescription(), locale));

                        List<ExportableParameter> exportableParameters = metaCommand.getMetaParams().stream()
                            .map((metaParam) -> {
                                ExportableParameter exportableParameter = new ExportableParameter();
                                exportableParameter.setLocale(locale);
                                exportableParameter.setName(resolver.getMessage(metaParam.getName(), locale));
                                exportableParameter.setDescription(resolver.getMessage(metaParam.getDescription(), locale));

                                return exportableParameter;
                            })
                            .collect(Collectors.toList());

                        exportableCommand.setParams(exportableParameters);
                        return exportableCommand;
                    })
                    .collect(Collectors.toList());

                exportableController.setCommands(exportableCommands);

                exportableControllers.putIfAbsent(locale, new ArrayList<>());
                exportableControllers.get(locale).add(exportableController);
            }
        }

        data.setControllers(exportableControllers);
        return data;
    }
}

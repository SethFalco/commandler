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

package org.elypia.commandler;

import org.elypia.commandler.api.Controller;
import org.elypia.commandler.config.*;
import org.elypia.commandler.metadata.*;

import javax.inject.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * The default help module, this is an optional module
 * one can add to provide some basic help functionality.
 *
 * @author seth@elypia.org (Seth Falco)
 */
@Singleton
public class DefaultHelpController implements Controller {

    private final ControllerConfig controllerConfig;
    private final ActivatorConfig activatorConfig;

    @Inject
    public DefaultHelpController(final ControllerConfig controllerConfig, final ActivatorConfig activatorConfig) {
        this.controllerConfig = controllerConfig;
        this.activatorConfig = activatorConfig;
    }

    public String getGroups() {
        List<MetaController> controllers = controllerConfig.getControllers();
        List<String> groups = controllers.stream()
            .filter(MetaController::isPublic)
            .map(MetaController::getGroup)
            .distinct()
            .collect(Collectors.toList());

        StringBuilder builder = new StringBuilder("Groups");

        for (String group : groups)
            builder.append("\n* ").append(group);

        return builder.toString();
    }

    public String getControllers(String query) {
        List<MetaController> controllers = controllerConfig.getControllers();
        List<MetaController> group = controllers.stream()
            .filter((c) -> c.getGroup().equalsIgnoreCase(query))
            .collect(Collectors.toList());

        if (group.isEmpty())
            return "There is no group by the name.";

        StringBuilder builder = new StringBuilder();

        for (MetaController controller : group) {
            builder.append(controller.getName()).append("\n").append(controller.getDescription());
            appendActivators(builder, controller);
            builder.append("\n\n");
        }

        return builder.toString();
    }

    /**
     * The default help command for a {@link Controller},
     * this should use the {@link MetaController} around
     * this {@link Controller} to display helpful information
     * to the user.
     *
     * @return The message to send to the end user.
     */
    public String getCommands(MetaController controller) {
        StringBuilder builder = new StringBuilder(controller.getName())
            .append("\n")
            .append(controller.getDescription());

        appendActivators(builder, controller);
        builder.append("\n\n");

        Iterator<MetaCommand> metaCommandIt = controller.getPublicCommands().iterator();

        while (metaCommandIt.hasNext()) {
            MetaCommand metaCommand = metaCommandIt.next();
            builder.append(metaCommand.getName())
                .append("\n")
                .append(metaCommand.getDescription());
            appendActivators(builder, metaCommand);
            List<MetaParam> metaParams = metaCommand.getMetaParams();

            metaParams.forEach((param) -> {
                builder.append("\n" + param.getName() + ": ");
                builder.append(param.getDescription());
            });

            if (metaCommandIt.hasNext())
                builder.append("\n\n");
        }

        return builder.toString();
    }

    private void appendActivators(final StringBuilder builder, final MetaComponent metaComponent) {
        activatorConfig.getActivators().forEach((forProperty, displayName) -> {
            String value = metaComponent.getProperty(forProperty);

            if (value != null)
                builder.append("\n").append(displayName).append(": ").append(value);
        });
    }
}

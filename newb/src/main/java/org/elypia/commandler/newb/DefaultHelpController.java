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

package org.elypia.commandler.newb;

import org.elypia.commandler.CommandlerExtension;
import org.elypia.commandler.annotation.*;
import org.elypia.commandler.annotation.command.StandardCommand;
import org.elypia.commandler.annotation.stereotypes.CommandController;
import org.elypia.commandler.api.Controller;
import org.elypia.commandler.dispatchers.StandardDispatcher;
import org.elypia.commandler.i18n.CommandlerMessageResolver;
import org.elypia.commandler.metadata.*;
import org.elypia.commandler.models.*;

import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

/**
 * The default help module, this is an optional module
 * one can add to provide some basic help functionality.
 *
 * This is pre-implemented it can only be created with knowledge
 * of Commandler as a framework, which we're not expecting new
 * consumers of the API to immediately be able to follow.
 *
 * You'll need to ensure you have a {@link org.elypia.commandler.api.Messenger}
 * implementation for the following types to use this effectively:
 * <ul>
 *     <li>{@link CommandModel}</li>
 *     <li>{@link GroupModel}</li>
 *     <li>{@link ControllerModel}</li>
 * </ul>
 *
 * @author seth@elypia.org (Seth Falco)
 */
@CommandController
@StandardCommand
public class DefaultHelpController implements Controller {

    protected final CommandlerExtension commmanderExtension;
    protected final CommandlerMessageResolver messageResolver;

    @Inject
    public DefaultHelpController(CommandlerExtension commmanderExtension, CommandlerMessageResolver messageResolver) {
        this.commmanderExtension = commmanderExtension;
        this.messageResolver = messageResolver;
    }

    @Default
    @StandardCommand
    public AllGroupsModel getAllGroups() {
        Map<String, List<ControllerModel>> groups = new HashMap<>();

        Collection<ControllerModel> allControllers = commmanderExtension.getMetaControllers().stream()
            .filter(MetaController::isPublic)
            .map(this::getControllerHelp)
            .collect(Collectors.toList());

        for (ControllerModel controllerModel : allControllers) {
            String group = messageResolver.getMessage(controllerModel.getGroup());

            if (!groups.containsKey(group))
                groups.put(group, new ArrayList<>());

            groups.get(group).add(controllerModel);
        }

        return new AllGroupsModel(groups);
    }

    @StandardCommand
    public Object getGroup(@Param String query) {
        Collection<MetaController> controllers = commmanderExtension.getMetaControllers();

        List<ControllerModel> group = controllers.stream()
            .filter(MetaController::isPublic)
            .filter((c) -> messageResolver.getMessage(c.getGroup()).equalsIgnoreCase(query) || messageResolver.getMessage(c.getProperty(StandardDispatcher.class, "aliases")).equalsIgnoreCase(query))
            .map(this::getControllerHelp)
            .collect(Collectors.toList());

        if (group.isEmpty())
            return "There is no group by the name.";

        String groupName = group.get(0).getGroup();

        return new GroupModel(groupName, group);
    }

    /**
     * The default help command for a {@link Controller},
     * this should use the {@link MetaController} around
     * this {@link Controller} to display helpful information
     * to the user.
     *
     * @param controller The {@link MetaController} to get commands for.
     * @return The message to send to the end user.
     */
    @StandardCommand
    public ControllerModel getControllerHelp(@Param MetaController controller) {
        String controllerName = messageResolver.getMessage(controller.getName());
        String controllerDescription = messageResolver.getMessage(controller.getDescription());
        String controllerGroup = messageResolver.getMessage(controller.getGroup());

        List<CommandModel> commands = new ArrayList<>();

        for (MetaCommand command : controller.getPublicCommands()) {
            String commandName = messageResolver.getMessage(command.getName());
            String commandDescription = messageResolver.getMessage(command.getDescription());

            List<ParamModel> params = new ArrayList<>();

            for (MetaParam metaParam : command.getMetaParams()) {
                String paramName = messageResolver.getMessage(metaParam.getName());
                String paramDescription = messageResolver.getMessage(metaParam.getDescription());
                ParamModel paramModel = new ParamModel(paramName, paramDescription);
                params.add(paramModel);
            }

            CommandModel commandModel = new CommandModel(commandName, commandDescription, params);
            commands.add(commandModel);
        }

        return new ControllerModel(controllerName, controllerDescription, controllerGroup, commands);
    }

        protected void appendActivators(final StringBuilder builder, final MetaComponent metaComponent) {
    //        activatorConfig.getActivators().forEach((forProperty, displayName) -> {
    //            String value = metaComponent.getProperty(forProperty);
    //
    //            if (value != null)
    //                builder.append("\n").append(displayName).append(": ").append(value);
    //        });
    }
}

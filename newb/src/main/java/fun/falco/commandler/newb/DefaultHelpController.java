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

package fun.falco.commandler.newb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;

import fun.falco.commandler.CommandlerExtension;
import fun.falco.commandler.annotation.Param;
import fun.falco.commandler.annotation.stereotypes.Controller;
import fun.falco.commandler.dispatchers.standard.StandardCommand;
import fun.falco.commandler.dispatchers.standard.StandardController;
import fun.falco.commandler.groups.Guidance;
import fun.falco.commandler.i18n.CommandlerMessageResolver;
import fun.falco.commandler.metadata.MetaCommand;
import fun.falco.commandler.metadata.MetaComponent;
import fun.falco.commandler.metadata.MetaController;
import fun.falco.commandler.metadata.MetaParam;
import fun.falco.commandler.models.AllGroupsModel;
import fun.falco.commandler.models.CommandModel;
import fun.falco.commandler.models.ControllerModel;
import fun.falco.commandler.models.GroupModel;
import fun.falco.commandler.models.ParamModel;
import fun.falco.commandler.models.PropertyModel;

/**
 * Default help module, this is an optional module one can add to provide some
 * basic help functionality.
 *
 * <p>This is pre-implemented it can only be created with knowledge of Commandler
 * as a framework, which we're not expecting new consumers of the API to
 * immediately be able to follow.</p>
 *
 * <p>Ensure you have a {@link fun.falco.commandler.api.Messenger}
 * implementation for the following types to use this effectively:
 * <ul>
 * <li>{@link CommandModel}</li>
 * <li>{@link GroupModel}</li>
 * <li>{@link ControllerModel}</li>
 * </ul></p>
 *
 * @author seth@falco.fun (Seth Falco)
 */
@Guidance
@StandardController
public class DefaultHelpController {

    protected final CommandlerExtension commandlerExtension;
    protected final CommandlerMessageResolver messageResolver;

    @Inject
    public DefaultHelpController(CommandlerExtension commandlerExtension, CommandlerMessageResolver messageResolver) {
        this.commandlerExtension = commandlerExtension;
        this.messageResolver = messageResolver;
    }

    @StandardCommand(isDefault = true)
    public AllGroupsModel getAllGroups() {
        Map<String, List<ControllerModel>> groups = new HashMap<>();

        Collection<ControllerModel> allControllers = commandlerExtension.getMetaControllers().stream()
            .filter(MetaController::isPublic)
            .map(this::getControllerHelp)
            .collect(Collectors.toList());

        for (ControllerModel controllerModel : allControllers) {
            String group = messageResolver.getMessage(controllerModel.getGroup());

            if (!groups.containsKey(group)) {
                groups.put(group, new ArrayList<>());
            }

            groups.get(group).add(controllerModel);
        }

        MetaController helpController = commandlerExtension.getMetaControllers().stream()
            .filter((meta) -> meta.getControllerType() == this.getClass())
            .findAny()
            .get();

        ControllerModel helpModel = getControllerHelp(helpController);
        return new AllGroupsModel(helpModel, groups);
    }

    @StandardCommand
    public Object getGroup(@Param String query) {
        Collection<MetaController> controllers = commandlerExtension.getMetaControllers();

        List<ControllerModel> group = controllers.stream()
            .filter(MetaController::isPublic)
            .filter((c) -> messageResolver.getMessage(c.getGroup()).equalsIgnoreCase(query))
            .map(this::getControllerHelp)
            .collect(Collectors.toList());

        if (group.isEmpty()) {
            return "There is no group by the name.";
        }

        String groupName = group.get(0).getGroup();

        return new GroupModel(groupName, group);
    }

    /**
     * Default help command for a {@link Controller}, this should use the
     * {@link MetaController} around this {@link Controller} to display helpful
     * information to the user.
     *
     * @param controller Controller to get commands for.
     * @return Message to send to the end user.
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
                ParamModel paramModel = new ParamModel(paramName, paramDescription, getLocalizedPublicProperties(metaParam));
                params.add(paramModel);
            }

            CommandModel commandModel = new CommandModel(commandName, commandDescription, params, getLocalizedPublicProperties(command));
            commands.add(commandModel);
        }

        return new ControllerModel(controllerName, controllerDescription, controllerGroup, commands, getLocalizedPublicProperties(controller));
    }

    protected List<PropertyModel> getLocalizedPublicProperties(final MetaComponent metaComponent) {
        List<PropertyModel> properties = new ArrayList<>();

        metaComponent.getProperties().forEach((key, property) -> {
            if (!property.isPublic()) {
                return;
            }

            if (property.isI18n()) {
                properties.add(new PropertyModel(messageResolver.getMessage(property.getDisplayName()), messageResolver.getMessage(property.getValue())));
            } else {
                properties.add(new PropertyModel(property.getDisplayName(), property.getValue()));
            }
        });

        return properties;
    }
}

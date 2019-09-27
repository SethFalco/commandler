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

package org.elypia.commandler.configuration;

import org.apache.commons.configuration2.ImmutableHierarchicalConfiguration;
import org.elypia.commandler.Commandler;
import org.elypia.commandler.api.*;
import org.elypia.commandler.metadata.*;

import javax.inject.*;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Convert the relevent parts of the {@link CommandlerConfiguration}
 * to the {@link org.elypia.commandler.Context} in order to contextualize
 * the metadata for the various components in {@link Commandler}.
 *
 * @author seth@elypia.org (Syed Shah)
 */
@Singleton
public class ContextConverter {

    private CommandlerConfiguration config;

    @Inject
    public ContextConverter(CommandlerConfiguration config) {
        this.config = config;
    }

    public List<Class<Integration<?, ?>>> convertIntegrations() {
        List<String> integrations = config.getList(String.class, "commandler.integrations");
        List<Class<Integration<?, ?>>> integrationTypes = new ArrayList<>();

        for (String integration : integrations) {
            Class<?> type;

            try {
                type = Class.forName(integration);

                if (Integration.class.isAssignableFrom(type))
                    integrationTypes.add((Class<Integration<?, ?>>)type);
                else
                    throw new RuntimeException();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        return integrationTypes;
    }

    public List<MetaController> convertControllers() {
        List<MetaController> metaControllers = new ArrayList<>();
        List<ImmutableHierarchicalConfiguration> controllers = config.getConfiguration().immutableConfigurationsAt("commandler.controller");

        for (ImmutableHierarchicalConfiguration controller : controllers) {
            ComponentConfig component = convertComponent(controller);
            Class<?> type;

            try {
                type = Class.forName(controller.getString("type"));
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

            String group = controller.getString("group");
            boolean hidden = controller.getBoolean("hidden", false);
            List<MetaCommand> commands = convertCommands(type, controller);
            metaControllers.add(new MetaController((Class<? extends Controller>)type, group, component.name, component.description, hidden, component.properties, commands));
        }

        return metaControllers;
    }

    public List<MetaCommand> convertCommands(Class<?> type, ImmutableHierarchicalConfiguration controller) {
        List<ImmutableHierarchicalConfiguration> commandsConfig = controller.immutableConfigurationsAt("command");

        List<MetaCommand> metaCommands = new ArrayList<>();

        for (ImmutableHierarchicalConfiguration command : commandsConfig) {
            ComponentConfig component = convertComponent(command);
            Method method = Arrays.stream(type.getMethods()).filter(m -> m.getName().equals(command.getString("method"))).findAny().orElseThrow();
            boolean hidden = command.getBoolean("hidden", false);
            boolean isStatic = command.getBoolean("static", false);
            boolean isDefault = command.getBoolean("default", false);
            List<MetaParam> params = convertParams(method, command);
            metaCommands.add(new MetaCommand(method, component.name, component.description, hidden, isStatic, isDefault, component.properties, params));
        }

        return metaCommands;
    }

    public List<MetaParam> convertParams(Method method, ImmutableHierarchicalConfiguration command) {
        List<ImmutableHierarchicalConfiguration> paramsConfig = command.immutableConfigurationsAt("param");

        List<MetaParam> metaParams = new ArrayList<>();

        for (ImmutableHierarchicalConfiguration param : paramsConfig) {
            ComponentConfig component = convertComponent(command);
            String defaultValue = param.getString("defaultValue");
            metaParams.add(new MetaParam(String.class, method.getParameters()[0], component.name, component.description, defaultValue, component.properties));
        }

        return metaParams;
    }

    public ComponentConfig convertComponent(ImmutableHierarchicalConfiguration component) {
        String name = component.getString("name");
        String description = component.getString("description");
        Properties properties = convertProperties(component);

        return new ComponentConfig(name, description, properties);
    }

    public Properties convertProperties(ImmutableHierarchicalConfiguration component) {
        List<ImmutableHierarchicalConfiguration> propertiesConfig = component.immutableConfigurationsAt("property");

        Properties properties = new Properties();

        for (ImmutableHierarchicalConfiguration property : propertiesConfig) {
            String key = property.getString("key");
            String value = property.getString("value");
            properties.put(key, value);
        }

        return properties;
    }

    public class ComponentConfig {

        private String name;
        private String description;
        private Properties properties;

        private ComponentConfig(String name, String description, Properties properties) {
            this.name = name;
            this.description = description;
            this.properties = properties;
        }
    }
}

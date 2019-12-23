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

package org.elypia.commandler.config;

import org.apache.commons.configuration2.ImmutableHierarchicalConfiguration;
import org.elypia.commandler.*;
import org.elypia.commandler.api.Controller;
import org.elypia.commandler.metadata.*;
import org.elypia.commandler.utils.ReflectionUtils;
import org.slf4j.*;

import javax.inject.*;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Convert everything relevent to {@link MetaController}s for
 * the {@link AppContext} in order to contextualize
 * the metadata for the various components in {@link Commandler}.
 *
 * <strong>This configuration object is <em>required</em>
 * when loading Commandler, Commandler will not work without it.</strong>
 *
 * @author seth@elypia.org (Seth Falco)
 */
@Singleton
public class ControllerConfig {

    private final static Logger logger = LoggerFactory.getLogger(ControllerConfig.class);

    /** A collection of data for each controller within this {@link Commandler} context. */
    private List<MetaController> controllers;

    /**
     * Create and instantiate this configuration object.
     * This is the default configuration object which is required by Commandler
     * for it to work and stores all core entities of a Commandler application.
     *
     * @param configService The configuration for this Commandler instance.
     */
    @Inject
    public ControllerConfig(final ConfigService configService) {
        controllers = new ArrayList<>();

        List<ImmutableHierarchicalConfiguration> controllerConfigs = configService.getConfiguration()
            .immutableConfigurationsAt("commandler.controller");

        for (ImmutableHierarchicalConfiguration controllerConfig : controllerConfigs) {
            ComponentConfig component = convertComponent(controllerConfig);
            String name = controllerConfig.getString("type");
            Class<Controller> type = ReflectionUtils.convertType(name, Controller.class);
            String group = controllerConfig.getString("group");
            boolean hidden = controllerConfig.getBoolean("hidden", false);
            List<MetaCommand> commands = convertCommands(type, controllerConfig);
            controllers.add(new MetaController(type, group, component.name, component.description, hidden, component.properties, commands));
        }
    }

    private List<MetaCommand> convertCommands(Class<?> type, ImmutableHierarchicalConfiguration controller) {
        List<ImmutableHierarchicalConfiguration> commandsConfig = controller.immutableConfigurationsAt("command");
        List<MetaCommand> commands = new ArrayList<>();

        for (ImmutableHierarchicalConfiguration commandConfig : commandsConfig) {
            ComponentConfig component = convertComponent(commandConfig);
            String methodName = commandConfig.getString("method");
            Method method = Arrays.stream(type.getMethods()).filter(m -> m.getName().equals(methodName)).findAny().orElseThrow(() ->
                new RuntimeException("Method #" + methodName + " doesn't exist in " + type + ".")
            );
            boolean hidden = commandConfig.getBoolean("hidden", false);
            boolean isStatic = commandConfig.getBoolean("static", false);
            boolean isDefault = commandConfig.getBoolean("default", false);
            List<MetaParam> params = convertParams(method, commandConfig);
            commands.add(new MetaCommand(method, component.name, component.description, hidden, isStatic, isDefault, component.properties, params));
        }

        return commands;
    }

    private List<MetaParam> convertParams(Method method, ImmutableHierarchicalConfiguration command) {
        List<ImmutableHierarchicalConfiguration> paramsConfig = command.immutableConfigurationsAt("param");
        List<MetaParam> params = new ArrayList<>();

        for (ImmutableHierarchicalConfiguration param : paramsConfig) {
            ComponentConfig component = convertComponent(param);
            int index = param.getInt("index", params.size());
            String defaultValue = param.getString("defaultValue");
            params.add(new MetaParam(index, method.getParameters()[index], component.name, component.description, defaultValue, component.properties));
        }

        return params;
    }

    private ComponentConfig convertComponent(ImmutableHierarchicalConfiguration component) {
        String name = component.getString("name");
        String description = component.getString("description");
        Properties properties = convertProperties(component);
        return new ComponentConfig(name, description, properties);
    }

    private Properties convertProperties(ImmutableHierarchicalConfiguration component) {
        List<ImmutableHierarchicalConfiguration> propertiesConfig = component.immutableConfigurationsAt("property");
        Properties properties = new Properties();

        for (ImmutableHierarchicalConfiguration property : propertiesConfig) {
            String key = property.getString("name");
            String value = property.getString("value");
            properties.put(key, value);
        }

        return properties;
    }

    public List<MetaController> getControllers() {
        return Collections.unmodifiableList(controllers);
    }

    /**
     * Private object to convert abstract data and put it in
     * the implementations.
     *
     * @author seth@elypia.org (Seth Falco)
     */
    private static class ComponentConfig {

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

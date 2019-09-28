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
import org.elypia.commandler.api.*;
import org.elypia.commandler.metadata.*;

import javax.inject.*;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Convert the relevent parts of the {@link ConfigService}
 * to the {@link AppContext} in order to contextualize
 * the metadata for the various components in {@link Commandler}.
 *
 * @author seth@elypia.org (Syed Shah)
 */
@Singleton
public class CommandlerConfig {

    /** A collection of data for each controller within this {@link Commandler} context. */
    private Collection<MetaController> controllers;

    /** A collection parameter adapters. */
    private Collection<MetaAdapter> adapters;

    /** A collection of response providers. */
    private Collection<MetaMessenger> messengers;

    private Collection<Class<Integration<?, ?>>> integrations;

    @Inject
    public CommandlerConfig(final ConfigService config) {
        this.controllers = convertControllers(config);
        this.adapters = convertAdapters(config);
        this.messengers = convertMessengers(config);
        this.integrations = convertIntegrations(config);
    }

    public List<Class<Integration<?, ?>>> convertIntegrations(final ConfigService config) {
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

    private List<MetaAdapter> convertAdapters(final ConfigService config) {
        List<ImmutableHierarchicalConfiguration> adapterConfig = config.getConfiguration().immutableConfigurationsAt("commandler.adapter");

        List<MetaAdapter> metaAdapters = new ArrayList<>();

        for (ImmutableHierarchicalConfiguration adapter : adapterConfig) {
            Class<Adapter<?>> type;

            try {
                type = (Class<Adapter<?>>)Class.forName(adapter.getString("type"));
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

            // TODO: Load compatible array.
            metaAdapters.add(new MetaAdapter(type));
        }

        return metaAdapters;
    }

    private List<MetaMessenger> convertMessengers(final ConfigService config) {
        List<ImmutableHierarchicalConfiguration> messengerConfig = config.getConfiguration().immutableConfigurationsAt("commandler.messenger");

        List<MetaMessenger> metaMessengers = new ArrayList<>();

        for (ImmutableHierarchicalConfiguration messenger : messengerConfig) {
            Class<ResponseBuilder<?, ?>> type;
            Class<?> provides;

            try {
                type = (Class<ResponseBuilder<?, ?>>)Class.forName(messenger.getString("type"));
                provides = Class.forName(messenger.getString("provides"));
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

            // TODO: Load compatible array.
            metaMessengers.add(new MetaMessenger(type, provides));
        }

        return metaMessengers;
    }

    private List<MetaController> convertControllers(final ConfigService config) {
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

    private List<MetaCommand> convertCommands(Class<?> type, ImmutableHierarchicalConfiguration controller) {
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

    private List<MetaParam> convertParams(Method method, ImmutableHierarchicalConfiguration command) {
        List<ImmutableHierarchicalConfiguration> paramsConfig = command.immutableConfigurationsAt("param");

        List<MetaParam> metaParams = new ArrayList<>();

        for (ImmutableHierarchicalConfiguration param : paramsConfig) {
            ComponentConfig component = convertComponent(command);
            String defaultValue = param.getString("defaultValue");
            metaParams.add(new MetaParam(String.class, method.getParameters()[0], component.name, component.description, defaultValue, component.properties));
        }

        return metaParams;
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
            String key = property.getString("key");
            String value = property.getString("value");
            properties.put(key, value);
        }

        return properties;
    }

    public Collection<MetaController> getControllers() {
        return controllers;
    }

    public Collection<MetaAdapter> getAdapters() {
        return adapters;
    }

    public Collection<MetaMessenger> getMessengers() {
        return messengers;
    }

    public Collection<Class<Integration<?, ?>>> getIntegrations() {
        return integrations;
    }

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

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
import org.elypia.commandler.utils.ReflectionUtils;

import javax.inject.*;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Convert the relevent parts of the {@link ConfigService}
 * to the {@link AppContext} in order to contextualize
 * the metadata for the various components in {@link Commandler}.
 *
 * <strong>This configuration object is <em>required</em>
 * when loading Commandler, Commandler will not work without it.</strong>
 *
 * @author seth@elypia.org (Syed Shah)
 */
@Singleton
public class CommandlerConfig {

    /** A collection of classes that represent object configuration. */
    private Collection<Class<Object>> configs;

    /** A collection of data for each controller within this {@link Commandler} context. */
    private Collection<MetaController> controllers;

    /** A collection parameter adapters. */
    private Collection<MetaAdapter> adapters;

    /** A collection of response providers. */
    private Collection<MetaMessenger> messengers;

    /** A collection of integrations that will be creates when this Commandler context is run. */
    private Collection<Class<Integration>> integrations;

    /** A collection of dispatchers that will be created when this Commandler context is run.  */
    private Collection<Class<Dispatcher>> dispatchers;

    /**
     * Create and instantiate this configuration object.
     * This is the default configuration object which is required by Commandler
     * for it to work and stores all core entities of a Commandler application.
     *
     * @param configService The configuration for this Commandler instance.
     */
    @Inject
    public CommandlerConfig(final ConfigService configService) {
        this.configs = convertConfigs(configService);
        this.controllers = convertControllers(configService);
        this.adapters = convertAdapters(configService);
        this.messengers = convertMessengers(configService);
        this.integrations = convertIntegrations(configService);
        this.dispatchers = convertDispatchers(configService);
    }

    private Collection<Class<Object>> convertConfigs(final ConfigService configService) {
        List<String> names = configService.getList(String.class, "commandler.config");
        return ReflectionUtils.convertTypes(names);
    }

    private Collection<Class<Integration>> convertIntegrations(final ConfigService configService) {
        List<String> names = configService.getList(String.class, "commandler.integration");
        return ReflectionUtils.convertTypes(names, Integration.class);
    }

    private Collection<Class<Dispatcher>> convertDispatchers(final ConfigService configService) {
        List<String> names = configService.getList(String.class, "commandler.dispatcher");
        return ReflectionUtils.convertTypes(names, Dispatcher.class);
    }

    private List<MetaAdapter> convertAdapters(final ConfigService configService) {
        List<ImmutableHierarchicalConfiguration> adapterConfigs = configService.getConfiguration()
            .immutableConfigurationsAt("commandler.adapter");
        List<MetaAdapter> adapters = new ArrayList<>();

        for (ImmutableHierarchicalConfiguration adapterConfig : adapterConfigs) {
            String name = adapterConfig.getString("type");
            Class<Adapter> adapterType = ReflectionUtils.convertType(name, Adapter.class);

            List<String> typeNames = adapterConfig.getList(String.class, "compatible");
            Collection<Class<Object>> compatibleTypes = ReflectionUtils.convertTypes(typeNames);

            adapters.add(new MetaAdapter(adapterType, compatibleTypes));
        }

        return adapters;
    }

    private List<MetaMessenger> convertMessengers(final ConfigService configService) {
        List<ImmutableHierarchicalConfiguration> messengerConfigs = configService.getConfiguration()
            .immutableConfigurationsAt("commandler.messenger");
        List<MetaMessenger> messengers = new ArrayList<>();

        for (ImmutableHierarchicalConfiguration messengerConfig : messengerConfigs) {
            String name = messengerConfig.getString("type");
            Class<ResponseBuilder> adapterType = ReflectionUtils.convertType(name, ResponseBuilder.class);

            String provides = messengerConfig.getString("provides");
            Class<Object> providesType = ReflectionUtils.convertType(provides);

            List<String> typeNames = messengerConfig.getList(String.class, "compatible");
            Collection<Class<Object>> compatibleTypes = ReflectionUtils.convertTypes(typeNames);

            messengers.add(new MetaMessenger(adapterType, providesType, compatibleTypes));
        }

        return messengers;
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
            String methodName = command.getString("method");
            Method method = Arrays.stream(type.getMethods()).filter(m -> m.getName().equals(methodName)).findAny().orElseThrow(() -> {
                return new RuntimeException("Method #" + methodName + " doesn't exist in " + type + ".");
            });
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
            String key = property.getString("name");
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

    public Collection<Class<Integration>> getIntegrations() {
        return integrations;
    }

    public Collection<Class<Object>> getConfigs() {
        return configs;
    }

    public Collection<Class<Dispatcher>> getDispatchers() {
        return dispatchers;
    }

    /**
     * Private object to  convert abstract data and put it in
     * the implementations.
     *
     * @author seth@elypia.org (Syed Shah)
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

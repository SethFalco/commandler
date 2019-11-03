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
import org.apache.commons.lang3.ClassUtils;
import org.elypia.commandler.*;
import org.elypia.commandler.api.*;
import org.elypia.commandler.metadata.*;
import org.elypia.commandler.utils.ReflectionUtils;
import org.slf4j.*;

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

    private final static Logger logger = LoggerFactory.getLogger(CommandlerConfig.class);

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

    private Collection<Class<MisuseHandler>> misuseHandlers;

    private Class<? extends ActionListener> actionListener;

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
        this.misuseHandlers = convertMisuseHandlers(configService);
        this.actionListener = getActionListener(configService);
    }

    private Class<? extends ActionListener> getActionListener(final ConfigService configService) {
        String name = configService.getString("commandler.action-handler");

        if (name == null)
            return ActionHandler.class;

        return ReflectionUtils.convertType(name, ActionListener.class);
    }

    private Collection<Class<Object>> convertConfigs(final ConfigService configService) {
        List<String> names = configService.getList(String.class, "commandler.config");

        if (names == null) {
            logger.debug("Commandler did not find any additional configurations defined in the configuration.");
            return List.of();
        }

        return ReflectionUtils.convertTypes(names);
    }

    private Collection<Class<Integration>> convertIntegrations(final ConfigService configService) {
        List<String> names = configService.getList(String.class, "commandler.integration");

        if (names == null) {
            logger.debug("Commandler did not find any integrations defined in the configuration.");
            return List.of();
        }

        return ReflectionUtils.convertTypes(names, Integration.class);
    }

    private Collection<Class<Dispatcher>> convertDispatchers(final ConfigService configService) {
        List<String> names = configService.getList(String.class, "commandler.dispatcher");

        if (names == null) {
            logger.debug("Commandler did not find any dispatchers defined in the configuration.");
            return List.of();
        }

        return ReflectionUtils.convertTypes(names, Dispatcher.class);
    }

    private Collection<Class<MisuseHandler>> convertMisuseHandlers(final ConfigService configService) {
        List<String> names = configService.getList(String.class, "commandler.misuse");

        if (names == null) {
            logger.debug("Commandler did not find any misuse handlers defined in the configuration.");
            return List.of();
        }

        return ReflectionUtils.convertTypes(names, MisuseHandler.class);
    }

    private List<MetaAdapter> convertAdapters(final ConfigService configService) {
        List<ImmutableHierarchicalConfiguration> adapterConfigs = configService.getConfiguration()
            .immutableConfigurationsAt("commandler.adapter");
        List<MetaAdapter> adapters = new ArrayList<>();

        for (ImmutableHierarchicalConfiguration adapterConfig : adapterConfigs) {
            String name = adapterConfig.getString("type");
            Class<Adapter> adapterType = ReflectionUtils.convertType(name, Adapter.class);

            List<String> typeNames = adapterConfig.getList(String.class, "compatible");
            Collection<Class<Object>> compatibleTypes = convertCompatible(typeNames);

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
            Collection<Class<Object>> compatibleTypes = convertCompatible(typeNames);

            messengers.add(new MetaMessenger(adapterType, providesType, compatibleTypes));
        }

        return messengers;
    }

    private Collection<Class<Object>> convertCompatible(List<String> typeNames) {
        Collection<Class<Object>> compatibleTypes = ReflectionUtils.convertTypes(typeNames);
        Set<Class<Object>> primitiveTypes = new HashSet<>();

        for (Class<Object> compatibleType : compatibleTypes) {
            Class<?> primitive = ClassUtils.wrapperToPrimitive(compatibleType);

            if (primitive != null)
                primitiveTypes.add((Class<Object>)primitive);
        }

        primitiveTypes.addAll(compatibleTypes);
        return primitiveTypes;
    }

    private List<MetaController> convertControllers(final ConfigService config) {
        List<ImmutableHierarchicalConfiguration> controllerConfigs = config.getConfiguration()
            .immutableConfigurationsAt("commandler.controller");
        List<MetaController> metaControllers = new ArrayList<>();

        for (ImmutableHierarchicalConfiguration controllerConfig : controllerConfigs) {
            ComponentConfig component = convertComponent(controllerConfig);
            String name = controllerConfig.getString("type");
            Class<Controller> type = ReflectionUtils.convertType(name, Controller.class);
            String group = controllerConfig.getString("group");
            boolean hidden = controllerConfig.getBoolean("hidden", false);
            List<MetaCommand> commands = convertCommands(type, controllerConfig);
            metaControllers.add(new MetaController(type, group, component.name, component.description, hidden, component.properties, commands));
        }

        return metaControllers;
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
            params.add(new MetaParam(method.getParameters()[index].getType(), method.getParameters()[index], component.name, component.description, defaultValue, component.properties));
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

    public Collection<Class<Object>> getConfigs() {
        return configs;
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

    public Collection<Class<Dispatcher>> getDispatchers() {
        return dispatchers;
    }

    public Collection<Class<MisuseHandler>> getMisuseHandlers() {
        return misuseHandlers;
    }

    public Class<? extends ActionListener> getActionListener() {
        return actionListener;
    }

    /**
     * Private object to convert abstract data and put it in
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

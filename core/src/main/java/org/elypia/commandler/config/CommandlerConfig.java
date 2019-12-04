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
import java.util.*;

/**
 * Convert the relevent parts of the {@link ConfigService}
 * to the {@link AppContext} in order to contextualize
 * the metadata for the various components in {@link Commandler}.
 *
 * <strong>This configuration object is <em>required</em>
 * when loading Commandler, Commandler will not work without it.</strong>
 *
 * @author seth@elypia.org (Seth Falco)
 */
@Singleton
public class CommandlerConfig {

    private final static Logger logger = LoggerFactory.getLogger(CommandlerConfig.class);

    /** A collection parameter adapters. */
    private Collection<MetaAdapter> adapters;

    /** A collection of response providers. */
    private Collection<MetaMessenger> messengers;

    /**
     * Create and instantiate this configuration object.
     * This is the default configuration object which is required by Commandler
     * for it to work and stores all core entities of a Commandler application.
     *
     * @param configService The configuration for this Commandler instance.
     */
    @Inject
    public CommandlerConfig(final ConfigService configService) {
        this.adapters = convertAdapters(configService);
        this.messengers = convertMessengers(configService);
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
            Class<Messenger> adapterType = ReflectionUtils.convertType(name, Messenger.class);

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

    public Collection<MetaAdapter> getAdapters() {
        return adapters;
    }

    public Collection<MetaMessenger> getMessengers() {
        return messengers;
    }
}

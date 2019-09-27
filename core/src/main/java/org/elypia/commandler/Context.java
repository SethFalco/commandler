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

import org.elypia.commandler.api.*;
import org.elypia.commandler.configuration.ContextConverter;
import org.elypia.commandler.metadata.*;
import org.slf4j.*;

import javax.inject.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Within the same {@link Context} a single {@link MetaController}
 * is only ever constructed once.
 *
 * @author seth@elypia.org (Syed Shah)
 */
@Singleton
public class Context implements Iterable<MetaController> {

    /** Logging using the SLF4J API. */
    private static final Logger logger = LoggerFactory.getLogger(Context.class);

    /** A collection of data for each controller within this {@link Commandler} context. */
    private Collection<MetaController> metaControllers;

    /** A collection parameter adapters. */
    private Collection<MetaAdapter> metaAdapters;

    /** A collection of response providers. */
    private Collection<MetaMessenger> metaMessengers;

    private Collection<Class<Integration<?, ?>>> integrations;

    @Inject
    public Context(final ContextConverter converter) {
        this.metaControllers = converter.convertControllers();
        integrations = converter.convertIntegrations();
        this.metaAdapters = new ArrayList<>();
        this.metaMessengers = new ArrayList<>();
    }

    public MetaController getModule(Class<? extends Controller> type) {
        for (MetaController module : metaControllers) {
            if (module.getHandlerType() == type)
                return module;
        }

        return null;
    }

    /**
     * Get an unmodifiable list of all modules including
     * private modules.
     *
     * @return A list of all modules.
     */
    public List<MetaController> getMetaControllers() {
        return getModules(true);
    }

    /**
     * Get a list of all modules within the context.
     *
     * @param includeHidden If to include private modules.
     * @return A list of modules.
     */
    public List<MetaController> getModules(boolean includeHidden) {
        return metaControllers.stream()
            .filter((m) -> !m.isHidden() || includeHidden)
            .sorted()
            .collect(Collectors.toUnmodifiableList());
    }

    public Collection<MetaAdapter> getMetaAdapters() {
        return Collections.unmodifiableCollection(metaAdapters);
    }

    public Collection<MetaMessenger> getMetaMessengers() {
        return Collections.unmodifiableCollection(metaMessengers);
    }

    /**
     * Get a group view of every module divided into their
     * individual groups. Modules in this list will have the
     * same reference as {@link #getMetaControllers()}.
     *
     * @return A unmodifiable map of modules and the groups they belong in.
     */
    public Map<String, List<MetaController>> getGroups() {
        return getGroups(true);
    }

    /**
     * Get a group view of all modules.
     *
     * @param includeHidden If to include public modules in the result.
     * @return A unmodifiable map of modules and the groups they belong in.
     */
    public Map<String, List<MetaController>> getGroups(boolean includeHidden) {
        Map<String, List<MetaController>> groups = metaControllers.stream()
            .filter((m) -> !m.isHidden() || includeHidden)
            .sorted()
            .collect(Collectors.groupingBy(MetaController::getGroup));

        return Collections.unmodifiableMap(groups);
    }

    /**
     * @see #getControls(boolean)
     * @return An unmodifiable collection of all commands.
     */
    public List<MetaCommand> getCommands() {
        return getControls(true);
    }

    /**
     * @param includeHidden If to include hidden modules and commands.
     * @return An unmodifiable collection of all commands.
     */
    public List<MetaCommand> getControls(boolean includeHidden) {
        return getModules(includeHidden).stream()
            .map((m) -> (includeHidden) ? m.getMetaCommands() : m.getPublicCommands())
            .flatMap(List::stream)
            .collect(Collectors.toUnmodifiableList());
    }

    public Collection<Class<Integration<?, ?>>> getIntegrations() {
        return integrations;
    }

    /**
     * @return An iterator of registered modules.
     */
    @Override
    public Iterator<MetaController> iterator() {
        return metaControllers.iterator();
    }
}

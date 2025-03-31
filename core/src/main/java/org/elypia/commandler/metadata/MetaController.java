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

package org.elypia.commandler.metadata;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.elypia.commandler.Commandler;
import org.elypia.commandler.annotation.stereotypes.Controller;

/**
 * This is a wrapper around a Module. This is not an instantiated module that
 * can process commands; it holds information on the commands in a
 * form convenient to access to {@link Commandler} and any other APIs.
 *
 * @author seth@falco.fun (Seth Falco)
 */
public class MetaController extends MetaComponent implements Comparable<MetaController>, Iterable<MetaCommand> {

    /** Class this annotation data belongs too. */
    private Class<?> controllerType;

    /** Group this module belongs to. */
    private String groupName;

    /** If the command should be hidden from documentation. */
    private boolean isHidden;

    /** List of {@link MetaCommand} that were created inside the {@link Controller}. */
    private List<MetaCommand> metaCommands;

    public MetaController(Class<?> controllerType, String groupName, String name, String description, boolean isHidden, Map<String, MetaProperty> properties, List<MetaCommand> metaCommands) {
        this.controllerType = Objects.requireNonNull(controllerType);
        this.groupName = Objects.requireNonNull(groupName);
        this.name = Objects.requireNonNull(name);
        this.description = description;
        this.isHidden = isHidden;
        this.properties = properties;
        this.metaCommands = Objects.requireNonNull(metaCommands);
    }

    public Class<?> getControllerType() {
        return controllerType;
    }

    public String getGroup() {
        return groupName;
    }

    public boolean isHidden() {
        return isHidden;
    }

    public boolean isPublic() {
        return !isHidden;
    }

    public List<MetaCommand> getMetaCommands() {
        return List.copyOf(metaCommands);
    }

    /**
     * @return
     *     All {@link MetaCommand}s in this module where
     *     {@link MetaCommand#isHidden()} is false.
     */
    public List<MetaCommand> getPublicCommands() {
        return metaCommands.stream()
            .filter(Predicate.not(MetaCommand::isHidden))
            .collect(Collectors.toUnmodifiableList());
    }

    /**
     * @return
     *     All {@link MetaCommand}s in this module where
     *     {@link MetaCommand#isHidden()} is true.
     */
    public List<MetaCommand> getHiddenCommands() {
        return metaCommands.stream()
            .filter(MetaCommand::isHidden)
            .collect(Collectors.toUnmodifiableList());
    }

    /**
     * Sorts {@link MetaController}s into alphabetical order.
     *
     * @param o Another module.
     * @return If this module is above or below the provided module.
     */
    @Override
    public int compareTo(MetaController o) {
        return name.compareToIgnoreCase(o.name);
    }

    @Override
    public Iterator<MetaCommand> iterator() {
        return metaCommands.iterator();
    }
}

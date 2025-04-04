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

package fun.falco.commandler.metadata;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

import fun.falco.commandler.api.Messenger;

/**
 * @author seth@falco.fun (Seth Falco)
 */
public class MetaMessenger {

    /** The type of the provider itself. */
    private Class<? extends Messenger> type;

    /** The class this type provides. */
    private Class<?> builds;

    /** The types this provider is compatible for. */
    private Collection<Class<?>> compatible;

    public MetaMessenger(Class<? extends Messenger> type, Class<?> builds, Class<?>... compatible) {
        this(type, builds, List.of(compatible));
    }

    public MetaMessenger(Class<? extends Messenger> type, Class<?> builds, Collection<Class<?>> compatible) {
        this.type = Objects.requireNonNull(type);
        this.builds = Objects.requireNonNull(builds);
        this.compatible = Objects.requireNonNull(compatible);
    }

    public Class<? extends Messenger> getProviderType() {
        return type;
    }

    public Class<?> getBuildType() {
        return builds;
    }

    public Collection<Class<?>> getCompatibleTypes() {
        return compatible;
    }
}

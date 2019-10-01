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

package org.elypia.commandler.metadata;

import org.elypia.commandler.api.ResponseBuilder;

import java.util.*;

/**
 * @author seth@elypia.org (Syed Shah)
 */
public class MetaMessenger {

    /** The type of the provider itself. */
    private Class<? extends ResponseBuilder> type;

    /** The class this type provides. */
    private Class<Object> builds;

    /** The types this provider is compatible for. */
    private Collection<Class<Object>> compatible;

    public MetaMessenger(Class<? extends ResponseBuilder> type, Class<Object> builds, Class<Object>... compatible) {
        this(type, builds, List.of(compatible));
    }

    public MetaMessenger(Class<? extends ResponseBuilder> type, Class<Object> builds, Collection<Class<Object>> compatible) {
        this.type = Objects.requireNonNull(type);
        this.builds = Objects.requireNonNull(builds);
        this.compatible = Objects.requireNonNull(compatible);
    }

    public Class<? extends ResponseBuilder> getProviderType() {
        return type;
    }

    public Class<Object> getBuildType() {
        return builds;
    }

    public Collection<Class<Object>> getCompatibleTypes() {
        return compatible;
    }
}

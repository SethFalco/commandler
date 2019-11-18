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

import org.elypia.commandler.api.Adapter;

import java.util.*;

/**
 * The metadata-data associated with a parameter adapters.
 *
 * @author seth@elypia.org (Seth Falco)
 */
public class MetaAdapter {

    /** The class of the adapters itself. */
    private Class<? extends Adapter> type;

    /** The compatible classes this adapters can adapt for. */
    private Collection<Class<Object>> compatible;

    public MetaAdapter(Class<? extends Adapter> type, Class<Object>... compatible) {
        this(type, List.of(compatible));
    }

    /**
     * @param type The class of the adapters itself.
     * @param compatible The compatible classes this adapters can adapt for.
     */
    public MetaAdapter(Class<? extends Adapter> type, Collection<Class<Object>> compatible) {
        this.type = Objects.requireNonNull(type);
        this.compatible = Objects.requireNonNull(compatible);
    }

    public Class<? extends Adapter> getAdapterType() {
        return type;
    }

    public Collection<Class<Object>> getCompatibleTypes() {
        return compatible;
    }
}

/*
 * Copyright 2019-2020 Elypia CIC
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

import org.elypia.commandler.api.ActionCache;
import org.elypia.commandler.event.Action;

import javax.enterprise.context.ApplicationScoped;
import java.io.Serializable;
import java.util.*;

/**
 * @author seth@elypia.org (Seth Falco)
 */
@ApplicationScoped
public class MemoryActionCache implements ActionCache {

    private final Map<Serializable, Action> actions;

    public MemoryActionCache() {
        actions = new HashMap<>();
    }

    @Override
    public void put(Action action) {
        actions.put(action.getId(), action);
    }

    @Override
    public Action get(Serializable serializable) {
        Objects.requireNonNull(serializable);
        return actions.get(serializable);
    }

    @Override
    public Action remove(Serializable serializable) {
        return actions.remove(serializable);
    }

    @Override
    public Collection<Action> getAll() {
        return Collections.unmodifiableCollection(actions.values());
    }
}

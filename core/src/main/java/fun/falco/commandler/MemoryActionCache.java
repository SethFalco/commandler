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

package fun.falco.commandler;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.enterprise.context.ApplicationScoped;

import fun.falco.commandler.api.ActionCache;
import fun.falco.commandler.event.Action;

/**
 * @author seth@falco.fun (Seth Falco)
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

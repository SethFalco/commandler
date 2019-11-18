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

import org.elypia.commandler.api.ActionCache;
import org.elypia.commandler.event.Action;

import java.io.Serializable;
import java.util.*;

/**
 * @author seth@elypia.org (Seth Falco)
 */
public class ListActionCache implements ActionCache {

    private final List<Action> actions;

    public ListActionCache() {
        actions = new ArrayList<>();
    }

    @Override
    public void put(Action action) {
        actions.add(action);
    }

    @Override
    public Action get(Serializable serializable) {
        Objects.requireNonNull(serializable);

        for (Action i : actions) {
            if (i.getId().equals(serializable))
                return i;
        }

        return null;
    }

    @Override
    public Action pop(Serializable serializable) {
        Action action = get(serializable);
        actions.remove(action);
        return action;
    }

    @Override
    public void remove(Serializable serializable) {
        for (int i = 0; i <= actions.size(); i++) {
            Action action = actions.get(i);

            if (action.getId().equals(serializable)) {
                actions.remove(action);
                return;
            }
        }
    }

    @Override
    public List<Action> getAll() {
        return Collections.unmodifiableList(actions);
    }
}

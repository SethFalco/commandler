package com.elypia.commandler;

import com.elypia.commandler.api.ActionCache;
import com.elypia.commandler.event.Action;

import java.io.Serializable;
import java.util.*;

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

package com.elypia.commandler.interfaces;

import com.elypia.commandler.CommandlerEvent;

@FunctionalInterface
public interface DynDefaultValue {
    String defaultValue(CommandlerEvent<?> event);
}

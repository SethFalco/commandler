package com.elypia.commandler.impl;

import com.elypia.commandler.CommandEvent;

public interface IBuilder<I, M> {
    M build(CommandEvent event, I input);
}

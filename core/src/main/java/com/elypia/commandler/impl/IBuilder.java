package com.elypia.commandler.impl;

import com.elypia.commandler.Commandler;
import com.elypia.commandler.events.*;

public interface IBuilder<I, M> {
    M build(CommandEvent event, I input);
}

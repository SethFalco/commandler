package com.elypia.commandler.impl;

public interface IBuilder<I, M> {
    M build(ICommandEvent<?, ?, M> event, I input);
}

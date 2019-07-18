package com.elypia.commandler.interfaces;

public interface Controller<M> {
    Class<M> getMessageType();
}

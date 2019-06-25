package com.elypia.commandler.metadata;

/**
 * @param <M> The MetaObject that this MetaBuilder will produce.
 * @param <B> The builder inheriting thie interface.
 */
public interface MetaBuilder<M, B extends MetaBuilder> {
    M build();
    B merge(B builder);
}

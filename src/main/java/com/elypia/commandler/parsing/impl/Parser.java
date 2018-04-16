package com.elypia.commandler.parsing.impl;

public interface Parser<T> {
    T parse(String input) throws IllegalArgumentException;
}

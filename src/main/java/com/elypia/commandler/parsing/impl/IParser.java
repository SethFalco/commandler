package com.elypia.commandler.parsing.impl;

public interface IParser<T> {
    T parse(String input) throws IllegalArgumentException;
}

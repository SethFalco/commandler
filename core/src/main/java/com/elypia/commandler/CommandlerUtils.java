package com.elypia.commandler;

import java.util.Iterator;

public final class CommandlerUtils {

    public static <T> T getLastElement(Iterator<T> iter) {
        T item = null;

        while (iter.hasNext())
            item = iter.next();

        return item;
    }
}


package com.elypia.commandler.utils;

import com.elypia.commandler.meta.data.MetaParam;

import java.util.StringJoiner;
import java.util.regex.Pattern;

public final class CommandlerUtils {

    private static final Pattern SPACE_SPLITTER = Pattern.compile("\\s+");

    public static String[] splitSpaces(final String body) {
        return splitSpaces(body, 0);
    }

    public static String[] splitSpaces(final String body, final int limit) {
        return SPACE_SPLITTER.split(body, limit);
    }

    public static String toParamString(MetaParam... params) {
        if (params == null)
            return "Uninitialized";

        StringJoiner itemJoiner = new StringJoiner(", ");

        for (MetaParam param : params) {
            String name = param.getName();
            StringBuilder builder = new StringBuilder();

            if (param.isOptional())
                builder.append("?");

            if (param.isList())
                builder.append("[").append(name).append("]");
            else
                builder.append(name);

            itemJoiner.add(builder.toString());
        }

        return "(" + params.length + ") " + itemJoiner.toString();
    }
}

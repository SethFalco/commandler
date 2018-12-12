package com.elypia.commandler.doc;

import com.elypia.commandler.doc.converters.ColorConverter;

import java.awt.*;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Pattern;

public final class DocUtils {

    private static final ColorConverter converter = new ColorConverter();
    private static final Map<Object, String> ids = new HashMap<>();

    public static String toRgba(Color color) {
        return converter.convertFromField(color);
    }

    public static Color toColor(String html) {
        return converter.convertToField(html);
    }

    public static String toOutput(String id) {
        final Pattern pattern = Pattern.compile("[^a-z\\d_-]+");
        return pattern.matcher(id.toLowerCase()).replaceAll("-");
    }

    public static String genId() {
        return genId(null);
    }

    public static <T> String genId(T item) {
        if (item != null && ids.containsKey(item))
            return ids.get(item);

        StringBuilder builder = new StringBuilder();
        Collection<String> values = ids.values();

        do {
            for (int i = 0; i < 6; i++)
                builder.append((char)ThreadLocalRandom.current().nextInt('a', 'z' + 1));

        } while (values.contains(builder.toString()));

        String result = builder.toString();

        if (item != null)
            ids.put(item, result);

        return result;
    }
}

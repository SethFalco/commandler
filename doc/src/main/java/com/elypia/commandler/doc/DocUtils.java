package com.elypia.commandler.doc;

import java.awt.*;
import java.util.regex.Pattern;

public final class DocUtils {

    public static String toRgba(Color color) {
        final String RGBA = "rgba(%s, %s, %s, %s)";

        Object[] values = {
            color.getRed(),
            color.getGreen(),
            color.getBlue(),
            color.getAlpha()
        };

        return String.format(RGBA, values);
    }

    public static String toOutput(String id) {
        final Pattern pattern = Pattern.compile("[^a-z\\d_-]+");
        return pattern.matcher(id.toLowerCase()).replaceAll("-");
    }
}

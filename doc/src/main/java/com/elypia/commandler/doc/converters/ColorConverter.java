package com.elypia.commandler.doc.converters;

import com.electronwill.nightconfig.core.conversion.Converter;
import com.elypia.commandler.doc.DocUtils;

import java.awt.*;
import java.util.regex.*;

public class ColorConverter implements Converter<Color, String> {
    
    private static final Pattern RGB = Pattern.compile("rgb\\(\\s*(\\d{1,3})\\s*,\\s*(\\d{1,3})\\s*,\\s*(\\d{1,3})\\s*\\)");
    private static final Pattern RGBA = Pattern.compile("rgba\\(\\s*(\\d{1,3})\\s*,\\s*(\\d{1,3})\\s*,\\s*(\\d{1,3})\\s*,\\s*(\\d{1,3})\\s*\\)");

    @Override
    public Color convertToField(String value) {
        Matcher rgbMatcher = RGB.matcher(value);

        if (rgbMatcher.find())
            return new Color(
                Integer.parseInt(rgbMatcher.group(1)),
                Integer.parseInt(rgbMatcher.group(2)),
                Integer.parseInt(rgbMatcher.group(3))
            );

        Matcher rgbaMatcher = RGBA.matcher(value);

        if (rgbaMatcher.find())
            return new Color(
                Integer.parseInt(rgbaMatcher.group(1)),
                Integer.parseInt(rgbaMatcher.group(2)),
                Integer.parseInt(rgbaMatcher.group(3)),
                Integer.parseInt(rgbaMatcher.group(4))
            );

        return Color.decode(value);
    }

    @Override
    public String convertFromField(Color value) {
        return DocUtils.toRgba(value);
    }
}

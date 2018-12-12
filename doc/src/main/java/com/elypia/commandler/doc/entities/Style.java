package com.elypia.commandler.doc.entities;

import com.electronwill.nightconfig.core.conversion.Path;

public class Style {

    @Path("color_scheme")
    private ColorScheme colors;

    @Path("font")
    private FontFamily font;

    public static Style getDefaultStyle() {
        return new Style(ColorScheme.getDarkSceme(), FontFamily.getSansSerif());
    }

    public Style() {
        // Do nothing
    }

    public Style(ColorScheme colors, FontFamily font) {
        this.colors = colors;
        this.font = font;
    }

    public ColorScheme getColors() {
        return colors;
    }

    public void setColors(ColorScheme colors) {
        this.colors = colors;
    }

    public FontFamily getFont() {
        return font;
    }

    public void setFont(FontFamily font) {
        this.font = font;
    }
}

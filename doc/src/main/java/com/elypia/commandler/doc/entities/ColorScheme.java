package com.elypia.commandler.doc.entities;

import com.electronwill.nightconfig.core.conversion.*;
import com.elypia.commandler.doc.converters.ColorConverter;

import java.awt.*;

public class ColorScheme {

    @Conversion(ColorConverter.class)
    @Path("primary")
    private Color primary;

    @Conversion(ColorConverter.class)
    @Path("secondary")
    private Color secondary;

    @Conversion(ColorConverter.class)
    @Path("tertiary")
    private Color tertiary;

    @Conversion(ColorConverter.class)
    @Path("text")
    private Color text;

    public static final ColorScheme LIGHT_SCHEME = new ColorScheme(
        new Color(150, 150, 150),
        new Color(200, 200,200),
        new Color(250, 250, 250),
        new Color(20, 20, 20)
    );

    public static final ColorScheme DARK_SCHEME = new ColorScheme(
        new Color(32, 32, 32),
        new Color(40, 40,40),
        new Color(59, 59, 59),
        new Color(255, 255, 255)
    );

    public ColorScheme() {
        // Do nothing
    }

    public ColorScheme(Color primary, Color secondary, Color tertiary, Color text) {
        this.primary = primary;
        this.secondary = secondary;
        this.tertiary = tertiary;
        this.text = text;
    }

    public Color getPrimary() {
        return primary;
    }

    public void setPrimary(Color primary) {
        this.primary = primary;
    }

    public Color getSecondary() {
        return secondary;
    }

    public void setSecondary(Color secondary) {
        this.secondary = secondary;
    }

    public Color getTertiary() {
        return tertiary;
    }

    public void setTertiary(Color tertiary) {
        this.tertiary = tertiary;
    }

    public Color getText() {
        return text;
    }

    public void setText(Color text) {
        this.text = text;
    }
}

package com.elypia.commandler.doc.entities;

import com.electronwill.nightconfig.core.conversion.*;
import com.elypia.commandler.doc.DocUtils;
import com.elypia.commandler.doc.converters.ColorConverter;

import java.awt.*;

public class Social {

    @Path("id")
    private String id;

    @Path("icon")
    private String icon;

    @Path("link")
    private String link;

    @Conversion(ColorConverter.class)
    @Path("color")
    private Color color;

    public Social() {
        // Do nothing
    }

    public Social(String icon, String link, Color color) {
        this.icon = icon;
        this.link = link;
        this.color = color;
    }

    public String getId() {
        if (id == null)
            id = DocUtils.genId();

        return id;
    }

    public Social setId(String id) {
        this.id = id;
        return this;
    }

    public String getIcon() {
        return icon;
    }

    public Social setIcon(String icon) {
        this.icon = icon;
        return this;
    }

    public String getLink() {
        return link;
    }

    public Color getColor() {
        return color;
    }
}

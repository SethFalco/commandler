package com.elypia.commandler.doc.entities;

import com.electronwill.nightconfig.core.conversion.Path;

public class Social {

    @Path("icon")
    private String icon;

    @Path("link")
    private String link;

    @Path("color")
    private String color;

    public Social() {
        // Do nothing
    }

    public Social(String icon, String link, String color) {
        this.icon = icon;
        this.link = link;
        this.color = color;
    }

    public String getIcon() {
        return icon;
    }

    public String getLink() {
        return link;
    }

    public String getColor() {
        return color;
    }
}

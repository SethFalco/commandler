package com.elypia.commandler.doc.entities;

import com.electronwill.nightconfig.core.conversion.Path;

public class Favicon {

    @Path("type")
    private String type;

    @Path("icon")
    private String icon;

    public Favicon() {
        // Do nothing
    }

    public Favicon(String type, String icon) {
        this.type = type;
        this.icon = icon;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}

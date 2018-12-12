package com.elypia.commandler.doc.entities;

import com.electronwill.nightconfig.core.conversion.Path;

public class FontFamily {

    @Path("name")
    private String name;

    @Path("link")
    private String location;

    public static FontFamily getSansSerif() {
        return new FontFamily("sans-serif");
    }

    public FontFamily() {
        // Do nothing
    }

    public FontFamily(String name) {
        this(name, null);
    }

    public FontFamily(String name, String location) {
        this.name = name;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}

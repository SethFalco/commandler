package com.elypia.commandler.doc;

public class SocialLink {

    private String icon;
    private String link;
    private String color;

    public SocialLink(String icon, String link, String color) {
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

package com.elypia.commandler.doc;

public class Link {

    /** Where this link should take the user. */
    private String url;

    /** The class to apply to the element displaying which should display an icon. */
    private String icon;

    public Link(final String url, final String icon) {
        this.url = url;
        this.icon = icon;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}

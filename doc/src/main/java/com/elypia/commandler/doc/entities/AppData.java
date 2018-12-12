package com.elypia.commandler.doc.entities;

import com.electronwill.nightconfig.core.conversion.*;
import com.electronwill.nightconfig.core.file.FileConfig;

import java.io.File;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class AppData {

    @Path("name")
    private String name;

    @Path("description")
    private String description;

    @Path("logo")
    private String logo;

    @Path("favicon")
    private Favicon favicon;

    @Path("social_links")
    private List<Social> social;

    @Path("style")
    private Style style;

    @Path("readme")
    private String readme;

    public AppData(URL url) {
        this(new File(URLDecoder.decode(url.getFile(), StandardCharsets.UTF_8)));
    }

    public AppData(File file) {
        FileConfig config = FileConfig.of(file);
        config.load();

        AppData data = new ObjectConverter().toObject(config, AppData::new);

        this.name = data.name;
        this.description = data.description;
        this.logo = data.logo;
        this.favicon = data.favicon;
        this.social = data.social;
        this.style = data.style;
        this.readme = data.readme;
    }

    public AppData() {
        this((String)null);
    }

    public AppData(String name) {
        this(name, null);
    }

    public AppData(String name, String description) {
        this(name, description, Style.getDefaultStyle());
    }

    public AppData(String name, String description, Style style) {
        this.name = name;
        this.description = description;
        this.style = style;
    }

    public String getName() {
        return name;
    }

    public AppData setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public AppData setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getLogo() {
        return logo;
    }

    public AppData setLogo(String logo) {
        this.logo = logo;
        return this;
    }

    public Favicon getFavicon() {
        return favicon;
    }

    public AppData setFavicon(Favicon favicon) {
        this.favicon = favicon;
        return this;
    }

    public List<Social> getSocial() {
        return social;
    }

    public AppData setSocial(List<Social> social) {
        this.social = social;
        return this;
    }

    public Style getStyle() {
        return style;
    }

    public AppData setStyle(Style style) {
        this.style = style;
        return this;
    }

    public String getReadme() {
        return readme;
    }

    public AppData setReadme(String readme) {
        this.readme = readme;
        return this;
    }
}

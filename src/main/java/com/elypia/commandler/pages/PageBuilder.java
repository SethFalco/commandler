package com.elypia.commandler.pages;

import com.elypia.commandler.Commandler;
import com.elypia.commandler.metadata.MetaModule;
import com.elypia.commandler.modules.CommandHandler;
import org.apache.velocity.*;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.*;
import java.util.*;

public class PageBuilder {

    private Commandler commandler;
    private Collection<MetaModule> modules;
    private Collection<CommandHandler> handlers;

    private String name;
    private String description;
    private String avatar;
    private String favicon;
    private String prefix;
    private boolean customPrefix;

    public PageBuilder(Commandler commandler) {
        this.commandler = Objects.requireNonNull(commandler);
        this.handlers = commandler.getHandlers();

        modules = new ArrayList<>();

        handlers.forEach(handler -> modules.add(handler.getModule()));
    }

    public void build(File file) throws IOException {
        Objects.requireNonNull(file);

        if (file.exists() && !file.isDirectory())
            throw new IllegalArgumentException("File must be a directory.");

        Properties properties = new Properties();
        properties.setProperty(VelocityEngine.RESOURCE_LOADER, "class");
        properties.setProperty("class.resource.loader.class", ClasspathResourceLoader.class.getName());

        VelocityEngine engine = new VelocityEngine(properties);
        engine.init();

        Template template = engine.getTemplate("pages/template.vm", "utf-8");

        VelocityContext context = new VelocityContext();
        context.put("name", name);
        context.put("description", description);
        context.put("avatar", avatar);
        context.put("favicon", favicon);
        context.put("prefix", prefix);
        context.put("handlers", handlers);
        context.put("metaModules", modules);

        String writePath = file.getAbsolutePath() + File.separator + "index.html";
        File toWrite = new File(writePath);
        file.mkdirs();

        String html = null;
        try (StringWriter writer = new StringWriter()) {
            template.merge(context, writer);
            html = writer.toString();
        }

        try (FileWriter writer = new FileWriter(toWrite)) {
            Document document = Jsoup.parse(html);
            writer.write(document.html());
        }
    }

    public PageBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public String getName() {
        return name;
    }

    public PageBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public PageBuilder setAvatar(String avatar) {
        this.avatar = avatar;
        return this;
    }

    public String getAvatar() {
        return avatar;
    }

    public PageBuilder setFavicon(String favicon) {
        this.favicon = favicon;
        return this;
    }

    public String getFavicon() {
        return favicon;
    }
}

package com.elypia.commandler.commandlerdoc;

import com.elypia.commandler.Commandler;
import com.elypia.commandler.impl.IHandler;
import com.elypia.commandler.metadata.MetaModule;
import org.apache.velocity.*;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.jsoup.Jsoup;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

public class Commandlerdoc {

    private static final String INCLUDE_PATH = "/pages/include/";

    private Properties velocityProperties;

    private Commandler commandler;
    private Collection<MetaModule> modules;
    private Collection<IHandler> handlers;

    private String name;
    private String description;
    private String avatar;
    private String favicon;
    private String prefix;
    private boolean customPrefix;

    public Commandlerdoc(Commandler commandler) {
        this.commandler = Objects.requireNonNull(commandler);
        this.handlers = commandler.getHandlers();
        modules = handlers.stream().map(IHandler::getModule).collect(Collectors.toList());

        velocityProperties = new Properties();
        velocityProperties.setProperty(VelocityEngine.RESOURCE_LOADER, "class");
        velocityProperties.setProperty("class.resource.loader.class", ClasspathResourceLoader.class.getName());
    }

    public void build(String path) throws IOException {
        Objects.requireNonNull(path);

        File file = new File(path);

        if (file.exists() && !file.isDirectory())
            throw new IllegalArgumentException("Path must be a directory.");

        VelocityEngine engine = new VelocityEngine(velocityProperties);
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

        try (StringWriter stringWriter = new StringWriter(); FileWriter fileWriter = new FileWriter(toWrite)) {
            template.merge(context, stringWriter);
            fileWriter.write(Jsoup.parse(stringWriter.toString()).html());
        }

        copyFiles("./pages", ".");
    }

    private void copyFiles(String output, String path) {
        File file = new File(output + "/" + path);
        file.mkdirs();

        try (InputStream inputStream = this.getClass().getResourceAsStream(INCLUDE_PATH + path)) {
            if (inputStream != null) {
                if (path.matches(".+\\..+")) {
                    Files.copy(inputStream, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                } else {
                    StringBuilder builder = new StringBuilder();

                    int c;
                    while ((c = inputStream.read()) != -1)
                        builder.append((char)c);

                    for (String string : builder.toString().split("\\s*\n\\s*"))
                        copyFiles(output, path + "/" + string);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Commandlerdoc setName(String name) {
        this.name = name;
        return this;
    }

    public String getName() {
        return name;
    }

    public Commandlerdoc setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Commandlerdoc setAvatar(String avatar) {
        this.avatar = avatar;
        return this;
    }

    public String getAvatar() {
        return avatar;
    }

    public Commandlerdoc setFavicon(String favicon) {
        this.favicon = favicon;
        return this;
    }

    public String getFavicon() {
        return favicon;
    }
}

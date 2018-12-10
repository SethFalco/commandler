package com.elypia.commandler.doc;

import com.elypia.commandler.*;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.doc.annotations.*;
import com.elypia.commandler.metadata.ModuleData;
import org.apache.velocity.*;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.jsoup.Jsoup;
import org.slf4j.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class DocBuilder {

    private static final Logger logger = LoggerFactory.getLogger(DocBuilder.class);

    private VelocityEngine engine;

    /**
     * The modules parsed as {@link ModuleData}.
     */
    private ModulesContext context;

    /**
     * All documentation specific data to help generate
     * webpages.
     *
     * This mostly comprises data that might not be required by
     * {@link Commandler} at runtime, but can be useful for
     * the application documentation.
     */
    private DocData data;

    private List<SocialLink> socialLinks;

    private MarkdownParser parser;

    /**
     * The id of the application.
     */
    private String name;

    /**
     * The description of the application.
     */
    private String description;

    /**
     * The logo for the application.
     */
    private String logo;

    /**
     * The favicon for the generated website.
     */
    private String favicon;

    private String favionType;

    public DocBuilder() {
        this("Documentation");
    }

    public DocBuilder(String name) {
        this(name, null);
    }

    public DocBuilder(String name, ModulesContext context) {
        this.name = name;
        this.context = context;

        parser = new MarkdownParser(TablesExtension.create());
    }

    /**
     * Get and return the velocity engine, initializing it
     * if it hasn't been initialized already.
     *
     * @return The velocity engine to build pages with.
     */
    private VelocityEngine getVelocityEngine() {
        if (engine == null) {
            Properties velocityProperties = new Properties();
            velocityProperties.setProperty(VelocityEngine.RESOURCE_LOADER, "class");
            velocityProperties.setProperty("class.resource.loader.class", ClasspathResourceLoader.class.getName());

            engine = new VelocityEngine(velocityProperties);
            engine.init();
        }

        return engine;
    }

    public void build() throws IOException {
        build("./build/docs/commandlerdoc/");
    }

    public void build(String path) throws IOException {
        Objects.requireNonNull(path);

        File file = new File(path);

        if (file.exists() && !file.isDirectory())
            throw new IllegalArgumentException("Path must be a directory.");

        VelocityEngine engine = getVelocityEngine();
        Template template = engine.getTemplate("template.vm", "utf-8");
        Map<String, List<ModuleData>> groups = context.getGroups(false);

        VelocityContext globalContext = new VelocityContext();
        globalContext.put("builder", this);
        globalContext.put("parser", parser);
        globalContext.put("app_name", name);
        globalContext.put("app_description", description);
        globalContext.put("app_logo", logo);
        globalContext.put("app_icon", favicon);
        globalContext.put("app_icon_type", favionType);
        globalContext.put("social_links", socialLinks);
        globalContext.put("all_modules", context.getModules(false));
        globalContext.put("groups", groups);
        globalContext.put("example_class", Example.class);
        globalContext.put("icon_class", Icon.class);

        VelocityContext homeContext = new VelocityContext(globalContext);
        homeContext.put("page_name", "Home");
        homeContext.put("content", parser.parseFile("./README.md"));

        outputFile(file.getAbsoluteFile(), "index", template, homeContext);

        VelocityContext allCommandsContext = new VelocityContext(globalContext);
        allCommandsContext.put("page_name", "All Commands");
        allCommandsContext.put("commands", context.getCommands(false));
        allCommandsContext.put("content", "all_commands_template.vm");

        outputFile(file.getAbsoluteFile(), "all-commands", template, allCommandsContext);

        for (var group : groups.entrySet()) {
            String groupName = group.getKey();
            List<ModuleData> modules = group.getValue();
            String outputName = "groups/" + toOutputFriendlyName(groupName);

            VelocityContext groupContext = new VelocityContext(globalContext);
            groupContext.put("page_name", "Group | " + groupName);
            groupContext.put("group_name", groupName);
            groupContext.put("group_modules", modules);
            groupContext.put("content", "group_template.vm");

            outputFile(file.getAbsoluteFile(), outputName, template, groupContext);
        }

        for (ModuleData module : context.getModules()) {
            if (!module.isPublic())
                continue;

            Module moduleAnno = module.getAnnotation();
            String moduleName = moduleAnno.id();
            String outputName = "modules/" + toOutputFriendlyName(moduleName);

            VelocityContext moduleContext = new VelocityContext(globalContext);
            moduleContext.put("page_name", "Module | " + moduleName);
            moduleContext.put("module", module);
            moduleContext.put("module_anno", moduleAnno);
            moduleContext.put("module_id", moduleAnno.id());
            moduleContext.put("module_group", moduleAnno.group());
            moduleContext.put("commands", module.getPublicCommands());
            moduleContext.put("content", "module_template.vm");

            outputFile(file.getAbsoluteFile(), outputName, template, moduleContext);
        }

        copyFiles(path, "/include");
    }

    public String toOutputFriendlyName(String id) {
        return id.toLowerCase().replaceAll("[^a-z\\d_-]+", "-");
    }

    private void outputFile(File file, String outputName, Template template, VelocityContext velocityContext) throws IOException {
        String writePath = file.getAbsolutePath() + "/" + outputName + ".html";
        File toWrite = new File(writePath);
        toWrite.getParentFile().mkdirs();

        try (StringWriter stringWriter = new StringWriter()) {
            try (FileWriter fileWriter = new FileWriter(toWrite)) {
                template.merge(velocityContext, stringWriter);

                String cleanHtml = Jsoup.parse(stringWriter.toString()).html();
                fileWriter.write(cleanHtml);
            }
        }
    }

    private void copyFiles(String ouput, String path) {
        copyFiles(ouput, path, path);
    }

    private void copyFiles(String output, String path, String start) {
        try (InputStream inputStream = this.getClass().getResourceAsStream(path)) {
            if (inputStream == null)
                return;

            if (path.matches(".+\\..+")) {
                if (!output.endsWith("/"))
                    output += "/";

                File file = new File(output + path.substring(start.length() + 1));
                file.mkdirs();

                Files.copy(inputStream, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } else {
                StringBuilder builder = new StringBuilder();

                int c;
                while ((c = inputStream.read()) != -1)
                    builder.append((char)c);

                for (String string : builder.toString().split("\\s*\n\\s*"))
                    copyFiles(output, path + File.separator + string, start);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ModulesContext getContext() {
        return context;
    }

    public DocBuilder setContext(ModulesContext context) {
        this.context = context;
        return this;
    }

    public DocBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public String getName() {
        return name;
    }

    public DocBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public DocBuilder setLogo(String logo) {
        this.logo = logo;
        return this;
    }

    public String getLogo() {
        return logo;
    }

    public DocBuilder setFavicon(String favionType, String favicon) {
        this.favionType = favionType;
        this.favicon = favicon;
        return this;
    }

    public String getFavicon() {
        return favicon;
    }

    public String getFavionType() {
        return favionType;
    }

    public List<SocialLink> getSocialLinks() {
        return socialLinks;
    }

    public DocBuilder setSocialLinks(List<SocialLink> socialLinks) {
        this.socialLinks = socialLinks;
        return this;
    }
}

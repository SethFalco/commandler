package com.elypia.commandler.doc;

import com.elypia.commandler.*;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.metadata.ModuleData;
import org.apache.velocity.*;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.commonmark.Extension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.jsoup.Jsoup;
import org.slf4j.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class DocBuilder {

    private static final Logger logger = LoggerFactory.getLogger(DocBuilder.class);

    private VelocityEngine engine;

    /**
     * All file names created by Commandlerdoc to ensure
     * there are no file id conflicts.
     */
    private Set<String> fileNames;

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

    public DocBuilder() {
        this("Documentation");
    }

    public DocBuilder(String name) {
        this(name, null);
    }

    public DocBuilder(String name, ModulesContext context) {
        this.name = name;
        this.context = context;

        fileNames = new HashSet<>();
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

        VelocityContext velocityContext = new VelocityContext();
        velocityContext.put("app_name", name);
        velocityContext.put("app_description", description);
        velocityContext.put("app_logo", logo);
        velocityContext.put("app_favicon", favicon);
        velocityContext.put("all_modules", context.getModules(false));
        velocityContext.put("all_groups", context.getGroups(false));
        velocityContext.put("this_name", "Home");

        List<Extension> extensions = List.of(TablesExtension.create());
        Parser parser = Parser.builder().extensions(extensions).build();
        Node document = parser.parseReader(new FileReader("./README.md"));
        HtmlRenderer renderer = HtmlRenderer.builder().extensions(extensions).build();

        String homepage = renderer.render(document);
        velocityContext.put("content", homepage);

        outputFile(file.getAbsoluteFile(), "index", template, velocityContext);

        velocityContext.put("module_page", true);

        for (ModuleData module : context.getModules()) {
            if (!module.isPublic())
                continue;

            String outputName = module.getAnnotation().id()
                    .toLowerCase()
                    .replaceAll("[^a-z\\d_-]+", "-");

            while (fileNames.contains(outputName))
                outputName += "_";

            velocityContext.put("this_module", module);

            Module annotation = module.getAnnotation();

            velocityContext.put("this_name", module.getAnnotation().id());
            velocityContext.put("this_anno", annotation);
            velocityContext.put("this_group", annotation.group());
            velocityContext.put("this_id", outputName);

            outputFile(file.getAbsoluteFile(), outputName, template, velocityContext);
        }

        copyFiles(path, "/include", "include");
    }

    private void outputFile(File file, String outputName, Template template, VelocityContext velocityContext) throws IOException {
        String writePath = file.getAbsolutePath() + File.separator + outputName + ".html";
        File toWrite = new File(writePath);
        file.mkdirs();

        try (StringWriter stringWriter = new StringWriter()) {
            try (FileWriter fileWriter = new FileWriter(toWrite)) {
                template.merge(velocityContext, stringWriter);

                String cleanHtml = Jsoup.parse(stringWriter.toString()).html();
                fileWriter.write(cleanHtml);
            }
        }
    }

    private void copyFiles(String output, String path, String name) {
        File file = new File(output + "/");
        file.mkdirs();

        try (InputStream inputStream = this.getClass().getResourceAsStream("" + path)) {
            if (inputStream != null) {
                if (path.matches(".+\\..+")) {
                    Files.copy(inputStream, Path.of(file.getAbsolutePath(), name), StandardCopyOption.REPLACE_EXISTING);
                } else {
                    StringBuilder builder = new StringBuilder();

                    int c;
                    while ((c = inputStream.read()) != -1)
                        builder.append((char)c);

                    for (String string : builder.toString().split("\\s*\n\\s*"))
                        copyFiles(output, path + "/" + string, string);
                }
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

    public DocBuilder setFavicon(String favicon) {
        this.favicon = favicon;
        return this;
    }

    public String getFavicon() {
        return favicon;
    }
}

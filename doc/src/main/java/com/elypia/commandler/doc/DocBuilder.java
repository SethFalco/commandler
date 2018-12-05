package com.elypia.commandler.doc;

import com.elypia.commandler.*;
import com.elypia.commandler.metadata.ModuleData;
import org.apache.velocity.*;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.jsoup.Jsoup;
import org.slf4j.*;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

public class DocBuilder {

    private static final Logger logger = LoggerFactory.getLogger(DocBuilder.class);

    private VelocityEngine engine;

    /**
     * All file names created by Commandlerdoc to ensure
     * there are no file name conflicts.
     */
    private Set<String> fileNames;

    /**
     * The modules parsed as {@link ModuleData}.
     */
    private ModuleContext context;

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
     * The name of the application.
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
        this.name = name;

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

        Template template = engine.getTemplate("pages/template.vm", "utf-8");
        VelocityContext context = new VelocityContext();
        context.put("name", name);
        context.put("description", description);
        context.put("logo", logo);
        context.put("favicon", favicon);

        for (ModuleData module : moduleData) {
            context.put("module", module);

            String outputName = module.getModule().name()
                .toLowerCase()
                .replaceAll("[\\s/\\\\]+", "-");

            String encodedName = URLEncoder.encode(outputName, StandardCharsets.UTF_8);

            while (fileNames.contains(encodedName))
                encodedName += "_";

            String writePath = file.getAbsolutePath() + File.separator + encodedName + ".html";
            File toWrite = new File(writePath);
            file.mkdirs();

            try (StringWriter stringWriter = new StringWriter()) {
                try (FileWriter fileWriter = new FileWriter(toWrite)) {
                    template.merge(context, stringWriter);

                    String cleanHtml = Jsoup.parse(stringWriter.toString()).html();
                    fileWriter.write(cleanHtml);
                }
            }
        }

        copyFiles("./pages", ".");
    }

    private void copyFiles(String output, String path) {
        File file = new File(output + "/" + path);
        file.mkdirs();

        try (InputStream inputStream = this.getClass().getResourceAsStream("" + path)) {
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

package com.elypia.commandler;

import com.elypia.commandler.impl.IHandler;
import com.elypia.commandler.metadata.ModuleData;
import org.slf4j.*;

import java.io.*;
import java.util.*;

public class ModuleContext {

    /**
     * Logging using the SLF4J API.
     */
    private static final Logger logger = LoggerFactory.getLogger(ModuleContext.class);

    /**
     * A collection of data for each module within this
     * module context.
     */
    protected Set<ModuleData> modules;

    /**
     * A list of grouped modules.
     */
    protected Map<String, Set<Class<IHandler>>> groups;

    /**
     * Add data for all modules found in the packages specified.
     *
     * @param packageNames A list of packages to load modules from.
     * @throws IOException If the package provided doesn't exist.
     */
    public void addPackage(String... packageNames) throws IOException {
        for (String packageName : packageNames) {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            String path = packageName.replace('.', File.separatorChar);

            loader.getResources(path).asIterator().forEachRemaining((url) -> {
                File dir = new File(url.getFile());
                addClasses(dir, packageName);
            });
        }
    }

    /**
     * Add all classes in the specified package recursivley.
     *
     * @param dir The directory we're loading from.
     * @param packageName The name of the package we're loading.
     */
    private void addClasses(File dir, String packageName) {
        File[] files = dir.listFiles();

        if (files == null)
            return;

        for (File file : files) {
            String fileFullName = file.getName();

            if (file.isDirectory())
                addClasses(file, packageName + "." + fileFullName);

            else if (fileFullName.endsWith(".class")) {
                String fileName = fileFullName.substring(0, fileFullName.length() - 6);

                try {
                    String className = packageName + "." + fileName;
                    Class<?> clazz = Class.forName(className);

                    if (!IHandler.class.isAssignableFrom(clazz))
                        logger.warn("Package contains type {} which is not assignable to {}.", clazz.getName(), IHandler.class);

                    else
                        modules.add(new ModuleData<>(clazz.asSubclass(IHandler.class)));

                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

package com.elypia.commandler;

import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.impl.IHandler;
import com.elypia.commandler.metadata.ModuleData;
import org.slf4j.*;

import java.io.*;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ModulesContext {

    /**
     * Logging using the SLF4J API.
     */
    private static final Logger logger = LoggerFactory.getLogger(ModulesContext.class);

    /**
     * A collection of data for each module within this
     * module context.
     */
    private Set<ModuleData> modules;

    /**
     * A list of grouped modules.
     */
    private Map<String, Set<Class<? extends IHandler>>> groups;

    /**
     * A list of all root aliases, this includes module aliases
     * and static command aliases. There must never be duplicates.
     */
    private Set<String> rootAliases;

    public ModulesContext() {
        modules = new HashSet<>();
        groups = new HashMap<>();
        rootAliases = new HashSet<>();
    }

    public void addModule(Class<? extends IHandler> clazz) {
        ModuleData data = new ModuleData<>(this, clazz);

        Module module = data.getModule();
        String group = module.group();

        groups.putIfAbsent(group, new HashSet<>());
        groups.get(group).add(clazz);
    }

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
                        addModule(clazz.asSubclass(IHandler.class));

                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public List<ModuleData> getModules() {
        return getModules(true);
    }

    /**
     * Get a list of all modules within the context.
     *
     * @param isPublic If to include public modules.
     * @return A list of modules.
     */
    public List<ModuleData> getModules(boolean isPublic) {
        if (isPublic)
            return List.copyOf(modules);

        return modules.stream()
            .filter(Predicate.not(ModuleData::isPublic))
            .collect(Collectors.toUnmodifiableList());
    }
}

package com.elypia.commandler;

import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.impl.IHandler;
import com.elypia.commandler.metadata.ModuleData;
import org.slf4j.*;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Within the same {@link ModulesContext} a single {@link ModuleData}
 * is only ever constructed once. All objects here are just various views
 * or references of the data.
 */
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
    private Map<String, Set<ModuleData>> groups;

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

        Module module = data.getAnnotation();
        String group = module.group();

        groups.putIfAbsent(group, new HashSet<>());
        groups.get(group).add(data);
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

    public ModuleData getModule(Class<? extends IHandler> clazz) {
        for (ModuleData data : modules) {
            if (data.getModuleClass() == clazz)
                return data;
        }

        return null;
    }

    /**
     * Get an unmodifiable list of all modules including
     * private modules with no documentation.
     *
     * @return A list of all modules.
     */
    public Set<ModuleData> getModules() {
        return getModules(true);
    }

    /**
     * Get a list of all modules within the context.
     *
     * @param isPublic If to include public modules.
     * @return A list of modules.
     */
    public Set<ModuleData> getModules(boolean isPublic) {
        if (isPublic)
            return Set.copyOf(modules);

        return modules.stream()
            .filter(ModuleData::isPublic)
            .collect(Collectors.toUnmodifiableSet());
    }

    /**
     * Get a group view of every module divided into their
     * individual groups. Modules in this list will have the
     * same reference as {@link #getModules()}.
     *
     * @return A unmodifiable map of modules and the groups they belong in.
     */
    public Map<String, Set<ModuleData>> getGroups() {
        return getGroups(true);
    }

    /**
     * Get a group view of all modules.
     *
     * @param isPublic If to include public modules in the result.
     * @return A unmodifiable map of modules and the groups they belong in.
     */
    public Map<String, Set<ModuleData>> getGroups(boolean isPublic) {
        if (isPublic)
            return Map.copyOf(groups);

        Map<String, Set<ModuleData>> groupsCopy = new HashMap<>();

        groups.forEach((group, modules) -> {
            Set<ModuleData> publicModules = modules.stream()
                .filter(ModuleData::isPublic)
                .collect(Collectors.toUnmodifiableSet());

            groupsCopy.put(group, publicModules);
        });

        return Map.copyOf(groupsCopy);
    }

    public Set<String> getAliases() {
        return Set.copyOf(rootAliases);
    }
}

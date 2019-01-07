package com.elypia.commandler;

import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.metadata.*;
import org.slf4j.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Within the same {@link ModulesContext} a single {@link ModuleData}
 * is only ever constructed once. All objects here are just various views
 * or references of the data.
 */
public class ModulesContext implements Iterable<ModuleData> {

    /**
     * Logging using the SLF4J API.
     */
    private static final Logger logger = LoggerFactory.getLogger(ModulesContext.class);

    /**
     * A collection of data for each module within this
     * module context.
     */
    private Map<Class<? extends Handler>, ModuleData> modules;

    /**
     * A list of grouped modules.
     */
    private Map<String, List<ModuleData>> groups;

    /**
     * A list of all root aliases, this includes module aliases
     * and static command aliases. There must never be duplicates.
     */
    private Set<String> rootAliases;

    public ModulesContext() {
        modules = new HashMap<>();
        groups = new TreeMap<>();
        rootAliases = new HashSet<>();
    }

    /**
     * Parses data from the class in order to create the {@link ModuleData}
     * entity which is a wrapper around the class, this also performs
     * all validation required to ensure the class is a valid {@link Module}.<br>
     * <strong>This does NOT instantiate the {@link Module}.</strong>
     *
     * @param classes The module to add to this context.
     */
    @SafeVarargs
    final public void addModules(Class<? extends Handler>... classes) {
        if (!Collections.disjoint(modules.keySet(), List.of(classes)))
            throw new IllegalStateException("Can't register a Handler that has already been registered.");

        for (Class<? extends Handler> clazz : classes) {
            ModuleData data = new ModuleData(this, clazz);
            Module module = data.getAnnotation();
            String group = module.group();

            modules.put(clazz, data);

            groups.putIfAbsent(group, new ArrayList<>());
            groups.get(group).add(data);

            rootAliases.addAll(data.getAliases());

            data.getStaticCommands().forEach(commandData -> {
                rootAliases.addAll(commandData.getAliases());
            });
        }
    }

    /**
     * Add data for all modules found in the packages specified.
     *
     * @param packageName A list of packages to load modules from.
     */
    public void addPackage(String packageName) {
        var modules = CommandlerUtils.getClasses(packageName, Handler.class);
        modules.forEach(this::addModules);
    }

    public ModuleData getModule(String alias) {
        for (ModuleData data : modules.values()) {
            if (data.performed(alias))
                return data;
        }

        return null;
    }

    public ModuleData getModule(Class<? extends Handler> clazz) {
        return modules.get(clazz);
    }

    /**
     * Get an unmodifiable list of all modules including
     * private modules with no documentation.
     *
     * @return A list of all modules.
     */
    public List<ModuleData> getModules() {
        return getModules(true);
    }

    /**
     * Get a list of all modules within the context.
     *
     * @param includePrivate If to include private modules.
     * @return A list of modules.
     */
    public List<ModuleData> getModules(boolean includePrivate) {
        if (includePrivate)
            return List.copyOf(modules.values());

        return modules.values().stream()
            .filter(ModuleData::isPublic)
            .sorted()
            .collect(Collectors.toUnmodifiableList());
    }

    /**
     * Get a group view of every module divided into their
     * individual groups. Modules in this list will have the
     * same reference as {@link #getModules()}.
     *
     * @return A unmodifiable map of modules and the groups they belong in.
     */
    public Map<String, List<ModuleData>> getGroups() {
        return getGroups(true);
    }

    /**
     * Get a group view of all modules.
     *
     * @param includePrivate If to include public modules in the result.
     * @return A unmodifiable map of modules and the groups they belong in.
     */
    public Map<String, List<ModuleData>> getGroups(boolean includePrivate) {
        if (includePrivate)
            return Map.copyOf(groups);

        Map<String, List<ModuleData>> groupsCopy = new TreeMap<>();

        groups.forEach((group, modules) -> {
            List<ModuleData> publicModules = modules.stream()
                .filter(ModuleData::isPublic)
                .sorted()
                .collect(Collectors.toUnmodifiableList());

            groupsCopy.put(group, publicModules);
        });

        return Collections.unmodifiableMap(groupsCopy);
    }

    public Set<String> getAliases() {
        return Set.copyOf(rootAliases);
    }

    public List<CommandData> getCommands() {
        return getCommands(true);
    }

    public List<CommandData> getCommands(boolean includePrivate) {
        List<CommandData> commands = new ArrayList<>();
        List<ModuleData> modules = getModules(includePrivate);

        modules.forEach((data) -> {
            if (includePrivate)
                commands.addAll(data.getCommands());
            else
                commands.addAll(data.getPublicCommands());
        });

        return List.copyOf(commands);
    }

    @Override
    public Iterator<ModuleData> iterator() {
        return modules.values().iterator();
    }
}

package com.elypia.commandler.metadata;

import com.elypia.commandler.Handler;
import com.elypia.commandler.metadata.data.*;
import org.slf4j.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Within the same {@link Context} a single {@link ModuleData}
 * is only ever constructed once. All objects here are just various views
 * or references of the data.
 */
public class Context implements Iterable<ModuleData> {

    /**
     * Logging using the SLF4J API.
     */
    private static final Logger logger = LoggerFactory.getLogger(Context.class);

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

    private Collection<ParserData> parsers;
    private Collection<BuilderData> builders;

    public Context(
        Collection<ModuleData> datas,
        Collection<ParserData> parsers,
        Collection<BuilderData> builders
    ) {
        modules = new HashMap<>();
        groups = new TreeMap<>();
        rootAliases = new HashSet<>();

        this.parsers = parsers;
        this.builders = builders;

        for (ModuleData data : datas) {
            Set<String> moduleAliases = data.getAliases();
            Set<String> staticAliases = data.getStaticCommands().parallelStream()
                .flatMap((c) -> c.getAliases().parallelStream())
                .collect(Collectors.toSet());

            rootAliases.addAll(moduleAliases);
            rootAliases.addAll(staticAliases);

            modules.put(data.getModuleClass(), data);

            if (!groups.containsKey(data.getGroup()))
                groups.put(data.getGroup(), new ArrayList<>());

            groups.get(data.getGroup()).add(data);
        }
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
            .filter(ModuleData::isHidden)
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
                .filter(ModuleData::isHidden)
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

    public Collection<ParserData> getParsers() {
        return parsers;
    }

    public Collection<BuilderData> getBuilders() {
        return builders;
    }
}

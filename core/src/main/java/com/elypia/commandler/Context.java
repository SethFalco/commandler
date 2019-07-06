package com.elypia.commandler;

import com.elypia.commandler.interfaces.Handler;
import com.elypia.commandler.metadata.*;
import org.slf4j.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Within the same {@link Context} a single {@link MetaModule}
 * is only ever constructed once.
 */
public class Context implements Iterable<MetaModule> {

    /** Logging using the SLF4J API. */
    private static final Logger logger = LoggerFactory.getLogger(Context.class);

    /** A distinct list of root aliases. (modules and static command aliases) */
    private Set<String> rootAliases;

    /** A collection of data for each module within this module context. */
    private Collection<MetaModule> modules;

    /** A collection parameter adapters. */
    private Collection<MetaAdapter> adapters;

    /** A collection of response providers. */
    private Collection<MetaProvider> providers;

    public Context(Collection<MetaModule> modules, Collection<MetaAdapter> adapters, Collection<MetaProvider> providers) {
        this.modules = Objects.requireNonNull(modules);
        this.adapters = Objects.requireNonNull(adapters);
        this.providers = Objects.requireNonNull(providers);
        this.rootAliases = new HashSet<>();

        for (MetaModule data : modules) {
            Set<String> moduleAliases = data.getAliases();
            Set<String> staticAliases = data.getStaticCommands().stream()
                .flatMap((c) -> c.getAliases().stream())
                .collect(Collectors.toSet());

            rootAliases.addAll(moduleAliases);
            rootAliases.addAll(staticAliases);
        }
    }

    public MetaModule getModule(String alias) {
        for (MetaModule module : modules) {
            if (module.performed(alias))
                return module;
        }

        return null;
    }

    public MetaModule getModule(Class<? extends Handler> type) {
        for (MetaModule module : modules) {
            if (module.getHandlerType() == type)
                return module;
        }

        return null;
    }

    /**
     * Get an unmodifiable list of all modules including
     * private modules with no documentation.
     *
     * @return A list of all modules.
     */
    public List<MetaModule> getModules() {
        return getModules(true);
    }

    /**
     * Get a list of all modules within the context.
     *
     * @param includeHidden If to include private modules.
     * @return A list of modules.
     */
    public List<MetaModule> getModules(boolean includeHidden) {
        return modules.stream()
            .filter((m) -> !m.isHidden() || includeHidden)
            .sorted()
            .collect(Collectors.toUnmodifiableList());
    }

    public Collection<MetaAdapter> getAdapters() {
        return Collections.unmodifiableCollection(adapters);
    }

    public Collection<MetaProvider> getProviders() {
        return Collections.unmodifiableCollection(providers);
    }

    /**
     * Get a group view of every module divided into their
     * individual groups. Modules in this list will have the
     * same reference as {@link #getModules()}.
     *
     * @return A unmodifiable map of modules and the groups they belong in.
     */
    public Map<String, List<MetaModule>> getGroups() {
        return getGroups(true);
    }

    /**
     * Get a group view of all modules.
     *
     * @param includePrivate If to include public modules in the result.
     * @return A unmodifiable map of modules and the groups they belong in.
     */
    public Map<String, List<MetaModule>> getGroups(boolean includePrivate) {
        Map<String, List<MetaModule>> groups = modules.stream()
            .filter((m) -> !m.isHidden() || includePrivate)
            .sorted()
            .collect(Collectors.groupingBy(MetaModule::getGroup));

        return Collections.unmodifiableMap(groups);
    }

    /**
     * @return A distinct unmodifiable set of all used aliases.
     */
    public Set<String> getAliases() {
        return Collections.unmodifiableSet(rootAliases);
    }

    /**
     * @see #getCommands(boolean)
     * @return An unmodifiable collection of all commands.
     */
    public List<MetaCommand> getCommands() {
        return getCommands(true);
    }

    /**
     * @param includeHidden If to include hidden modules and commands.
     * @return An unmodifiable collection of all commands.
     */
    public List<MetaCommand> getCommands(boolean includeHidden) {
        return getModules(includeHidden).stream()
            .map((m) -> (includeHidden) ? m.getCommands() : m.getPublicCommands())
            .flatMap(List::stream)
            .collect(Collectors.toUnmodifiableList());
    }

    /**
     * @return An iterator of registered modules.
     */
    @Override
    public Iterator<MetaModule> iterator() {
        return modules.iterator();
    }
}

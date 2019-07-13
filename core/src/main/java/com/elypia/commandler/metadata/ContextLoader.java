package com.elypia.commandler.metadata;

import com.elypia.commandler.Context;
import com.elypia.commandler.interfaces.MetaLoader;
import com.elypia.commandler.metadata.builders.*;
import org.slf4j.*;

import java.util.*;
import java.util.stream.Collectors;

public class ContextLoader {

    private static final Logger logger = LoggerFactory.getLogger(ContextLoader.class);

    private final Collection<ModuleBuilder> moduleBuilders;
    private final Collection<AdapterBuilder> adapterBuilders;
    private final Collection<ProviderBuilder> providerBuilders;

    /** This {@link ContextLoader} will no longer allow new MetadataLoaders if true. */
    private boolean locked;

    public ContextLoader(Collection<MetaLoader> loaders) {
        this(loaders.toArray(MetaLoader[]::new));
    }

    public ContextLoader(MetaLoader... loaders) {
        moduleBuilders = new ArrayList<>();
        adapterBuilders = new ArrayList<>();
        providerBuilders = new ArrayList<>();

        load(loaders);
    }

    // TODO: This isn't using the merge method so it'll just add lots of duplicate builders
    public ContextLoader load(MetaLoader... loaders) {
        if (locked)
            throw new IllegalStateException("This context is locked, you can not load more data once it's started building.");

        for (MetaLoader loader : loaders) {
            List<ModuleBuilder> moduleBuilders = loader.getModules();
            logger.debug("{} succesfully loaded {} modules.", loader, moduleBuilders.size());

            for (ModuleBuilder moduleBuilder : moduleBuilders) {
                List<CommandBuilder> commandBuilders = loader.getCommands(moduleBuilder.getHandlerType());

                for (CommandBuilder commandBuilder : commandBuilders) {
                    List<ParamBuilder> paramBuilders = loader.getParams(commandBuilder.getMethod());
                    commandBuilder.setParams(paramBuilders);
                }

                moduleBuilder.setCommands(commandBuilders);
            }

            this.moduleBuilders.addAll(moduleBuilders);
            this.adapterBuilders.addAll(loader.getAdapters());
            this.providerBuilders.addAll((loader.getProviders()));
        }

        return this;
    }

    public Context build() {
        locked = true;
        List<MetaModule> modules = new ArrayList<>();

        for (ModuleBuilder builder : moduleBuilders)
            modules.add(builder.build(modules.toArray(MetaModule[]::new)));

        List<MetaAdapter> adapters = adapterBuilders.stream()
            .map(AdapterBuilder::build)
            .collect(Collectors.toUnmodifiableList());

        List<MetaProvider> providers = providerBuilders.stream()
            .map(ProviderBuilder::build)
            .collect(Collectors.toUnmodifiableList());

        return new Context(modules, adapters, providers);
    }

    public Collection<ModuleBuilder> getModuleBuilders() {
        return Collections.unmodifiableCollection(moduleBuilders);
    }

    public Collection<AdapterBuilder> getAdapterBuilders() {
        return Collections.unmodifiableCollection(adapterBuilders);
    }

    public Collection<ProviderBuilder> getProviderBuilders() {
        return Collections.unmodifiableCollection(providerBuilders);
    }
}

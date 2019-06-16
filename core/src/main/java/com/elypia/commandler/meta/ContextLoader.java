package com.elypia.commandler.meta;

import com.elypia.commandler.core.Context;
import com.elypia.commandler.interfaces.MetaLoader;
import com.elypia.commandler.meta.builder.*;
import com.elypia.commandler.meta.data.*;
import org.slf4j.*;

import java.util.*;
import java.util.stream.Collectors;

public class ContextLoader {

    private static final Logger logger = LoggerFactory.getLogger(ContextLoader.class);

    /** The loaders to obtain metadata for this Context from. */
    private final MetaLoader[] loaders;

    private final Collection<ModuleBuilder> moduleBuilders;
    private final Collection<AdapterBuilder> adapterBuilders;
    private final Collection<ProviderBuilder> providerBuilders;

    public ContextLoader(MetaLoader... loaders) {
        this.loaders = loaders;
        moduleBuilders = new ArrayList<>();
        adapterBuilders = new ArrayList<>();
        providerBuilders = new ArrayList<>();
    }

    // TODO: This isn't using the merge method so it'll just add lots of duplicate builders
    public ContextLoader load() {
        for (MetaLoader loader : loaders) {
            List<ModuleBuilder> moduleBuilders = loader.getModules();

            for (ModuleBuilder moduleBuilder : moduleBuilders) {
                List<CommandBuilder> commandBuilders = loader.getCommands(moduleBuilder.getHandlerType());

                for (CommandBuilder commandBuilder : commandBuilders) {
                    List<ParamBuilder> paramBuilders = loader.getParams(null);
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
        List<MetaModule> modules = moduleBuilders.stream()
            .map((m) -> m.build(this))
            .sorted()
            .collect(Collectors.toUnmodifiableList());

        List<MetaAdapter> adapters = adapterBuilders.stream()
            .map(AdapterBuilder::build)
            .collect(Collectors.toUnmodifiableList());

        List<MetaProvider> providers = providerBuilders.stream()
            .map(ProviderBuilder::build)
            .collect(Collectors.toUnmodifiableList());

        return new Context(modules, adapters, providers);
    }
}

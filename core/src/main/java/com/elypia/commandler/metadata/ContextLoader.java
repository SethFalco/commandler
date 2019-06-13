package com.elypia.commandler.metadata;

import com.elypia.commandler.Handler;
import com.elypia.commandler.adapters.*;
import com.elypia.commandler.def.modules.HelpModule;
import com.elypia.commandler.exceptions.ConflictingModuleException;
import com.elypia.commandler.interfaces.*;
import com.elypia.commandler.metadata.builder.*;
import com.elypia.commandler.metadata.data.MetaModule;
import com.elypia.commandler.metadata.loader.*;
import org.slf4j.*;

import java.io.*;
import java.lang.reflect.*;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

public class ContextLoader {

    private static final Logger logger = LoggerFactory.getLogger(ContextLoader.class);

    private MetadataLoader[] loaders;

    private Collection<Class<? extends Handler>> handlers;
    private Collection<Class<? extends Adapter>> parsers;
    private Collection<Class<? extends Provider>> builders;

    private Collection<MetaModule> modules;

    private Collection<ModuleBuilder> moduleBuilders;
    private Collection<AdapterBuilder> adapterBuilders;
    private Collection<ProviderBuilder> providerBuilders;

    /**
     * Defaults to using all provided loaders which will cascade to load
     * metadata with a first come first serve approach.
     *
     * @param reference A reference class for the {@link AnnotationLoader} and
     * {@link ReflectionLoader} to use to search for potential classes that
     * are moduleBuilders.
     */
    public ContextLoader(Class reference) {
        this(reference, new AnnotationLoader()); //, new ReflectionLoader()
    }

    public ContextLoader(MetadataLoader... loaders) {
        this(null, loaders);
    }

    public ContextLoader(Class reference, MetadataLoader... loaders) {
        this.loaders = loaders;

        handlers = new ArrayList<>();
        builders = new ArrayList<>();
        parsers = new ArrayList<>();

        modules = new ArrayList<>();

        moduleBuilders = new ArrayList<>();
        adapterBuilders = new ArrayList<>();
        providerBuilders = new ArrayList<>();

        add(
            HelpModule.class,
            BooleanAdapter.class,
            CharAdapter.class,
            DurationAdapter.class,
            EnumAdapter.class,
            NumberAdapter.class,
            StringAdapter.class,
            UrlAdapter.class
        );

        if (reference == null)
            return;

        add(getClasses(reference));
    }

    public void add(Class... classes) {
        add(List.of(classes));
    }

    public void add(Iterable<Class> clazzes) {
        for (Class clazz : clazzes) {
            if (Handler.class.isAssignableFrom(clazz))
                handlers.add(clazz);
            else if (Adapter.class.isAssignableFrom(clazz))
                parsers.add(clazz);
            else if (Provider.class.isAssignableFrom(clazz))
                builders.add(clazz);
        }
    }

    public Context load() {
        // TODO: Get data as adapters and merge them to prevent overwriting
        for (MetadataLoader loader : loaders) {
            for (Class<? extends Handler> clazz : handlers) {
                ModuleBuilder module = new ModuleBuilder(clazz);
                loader.loadModule(module);

                for (Method method : loader.findCommands(clazz)) {
                    CommandBuilder command = new CommandBuilder(method);
                    loader.loadCommand(command);

                    for (Parameter parameter : loader.findParams(method)) {
                        ParamBuilder param = new ParamBuilder(parameter);
                        loader.loadParam(param);
                        command.addParam(param);
                    }

                    for (Method methodOverload : loader.findOverloads(command)) {
                        OverloadBuilder overload = new OverloadBuilder(methodOverload);
                        loader.loadOverload(overload);

                        for (Parameter parameter : loader.findParams(method)) {
                            ParamBuilder param = new ParamBuilder(parameter);
                            loader.loadParam(param);
                            overload.addParam(param);
                        }

                        command.addOverload(overload);
                    }

                    module.addCommand(command);
                }

                moduleBuilders.add(module);
            }

            for (Class<? extends Adapter> clazz : parsers) {
                AdapterBuilder builder = new AdapterBuilder(clazz);
                loader.loadParser(builder);
                adapterBuilders.add(builder);
            }

            for (Class<? extends Provider<?, ?>> clazz : builders) {
                ProviderBuilder builder = new ProviderBuilder(clazz);
                loader.loadBuilder(builder);
                providerBuilders.add(builder);
            }
        }

        for (ModuleBuilder builder : moduleBuilders) {
            try {
                modules.add(builder.build(this));
            } catch (ConflictingModuleException ex) {
                logger.warn("Module {} not loaded due to conflicts with other modules.", builder.getHandlerClass());
            }
        }

        var parsers = adapterBuilders.stream()
            .map((p) -> p.build(this))
            .collect(Collectors.toList());

        var builders = providerBuilders.stream()
            .map((b) -> b.build(this))
            .collect(Collectors.toList());

        return new Context(modules, parsers, builders);
    }

    /**
     * Add data for all modules found in the packages specified.
     *
     * @param reference Class is used as a reference for the root package to search.
     */
    public static Collection<Class> getClasses(Class reference) {
        Collection<Class> list = new ArrayList<>();

        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        String packageName = reference.getPackageName();
        String path = packageName.replace('.', File.separatorChar);

        try {
            loader.getResources(path).asIterator().forEachRemaining((url) -> {
                try {
                    File dir = new File(url.toURI());
                    addClasses(dir, packageName, list);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }

    /**
     * Add all classes in the specified package recursivley.
     *
     * @param dir The directory we're loading from.
     * @param packageName The name of the package we're loading.
     */
    private static void addClasses(File dir, String packageName, Collection<Class> list) {
        File[] files = dir.listFiles();

        if (files == null)
            return;

        for (File file : files) {
            String fileFullName = file.getName();

            if (file.isDirectory())
                addClasses(file, packageName + "." + fileFullName, list);

            else if (fileFullName.endsWith(".class")) {
                String fileName = fileFullName.substring(0, fileFullName.length() - 6);

                try {
                    Class clazz = Class.forName(packageName + "." + fileName);
                    int modifiers = clazz.getModifiers();

                    if (Modifier.isInterface(modifiers) || Modifier.isAbstract(modifiers))
                        continue;

                    list.add(clazz);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public Collection<ModuleBuilder> getModuleBuilders() {
        return moduleBuilders;
    }

    public Collection<MetaModule> getModules() {
        return modules;
    }
}

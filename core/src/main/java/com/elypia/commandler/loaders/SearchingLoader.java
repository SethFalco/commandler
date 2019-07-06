package com.elypia.commandler.loaders;

import com.elypia.commandler.interfaces.MetaLoader;
import com.elypia.commandler.utils.ReflectionUtils;

import java.io.IOException;
import java.util.*;

public abstract class SearchingLoader implements MetaLoader, Iterable<Class<?>> {

    /** All classes found when searching. */
    protected Collection<Class<?>> types;

    public SearchingLoader() {
        this.types = new ArrayList<>();
    }

    public SearchingLoader(Package... pkge) {
        types = new ArrayList<>();

        for (Package p : pkge)
            types.addAll(ReflectionUtils.getClasses(p));
    }

    /**
     * It's strongly recommend not to use overload but is available
     * if required.
     *
     * @param pkge
     */
    public SearchingLoader(String... pkge) throws IOException {
        types = new ArrayList<>();

        for (String p : pkge)
            types.addAll(ReflectionUtils.getClasses(p));
    }

    /**
     * @return A list of all types that were found when searching for classes.
     */
    public Collection<Class<?>> getTypes() {
        return types;
    }

    @Override
    public Iterator<Class<?>> iterator() {
        return types.iterator();
    }
}

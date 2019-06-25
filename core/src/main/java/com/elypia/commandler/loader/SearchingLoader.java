package com.elypia.commandler.loader;

import com.elypia.commandler.interfaces.MetaLoader;
import com.elypia.commandler.utils.ReflectionUtils;

import java.io.IOException;
import java.util.*;

public abstract class SearchingLoader implements MetaLoader, Iterable<Class<?>> {

    /** All classes found when searching. */
    protected Collection<Class<?>> types;

    // TODO: This doesn't belong here, a SEARCH loader shouldn't have a means to not search
    /**
     * Bypass searching and only add the specified classes.
     *
     * @param types The types to add.
     */
    public SearchingLoader(Class<?>... types) {
        this.types = List.of(types);
    }

    public SearchingLoader(Package pkge) {
        this.types = ReflectionUtils.getClasses(pkge);
    }

    /**
     * It's strongly recommend not to use overload but is available
     * if required.
     *
     * @param pkge
     */
    public SearchingLoader(String pkge) throws IOException {
        types = ReflectionUtils.getClasses(pkge);
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

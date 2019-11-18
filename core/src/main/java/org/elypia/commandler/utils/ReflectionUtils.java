/*
 * Copyright 2019-2019 Elypia CIC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.elypia.commandler.utils;

import org.slf4j.*;

import java.io.*;
import java.lang.reflect.Modifier;
import java.net.*;
import java.util.*;

/**
 * Centralized and reusable utilities for reflection.
 *
 * @author seth@elypia.org (Seth Falco)
 */
public final class ReflectionUtils {

    /** Logging with slf4j; make sure a implenting or binding is available at runtime. */
    private static final Logger logger = LoggerFactory.getLogger(ReflectionUtils.class);

    /** <strong>Only use the methods in this class statically.</strong> */
    private ReflectionUtils() {
        // Don't construct this class.
    }

    public static Collection<Class<Object>> convertTypes(final String[] names) {
        return convertTypes(List.of(names));
    }

    public static Collection<Class<Object>> convertTypes(final Collection<String> names) {
        return convertTypes(names, Object.class);
    }

    public static <T> Collection<Class<T>> convertTypes(final String[] names, final Class<T> type) {
        return convertTypes(List.of(names), type);
    }

    /**
     * Convert a list of class names to a list of classes.
     *
     * @param names All the class names to convert to classes.
     * @param type The type of object each class is expected to be a class of.
     * @param <T> The type of {@link Collection} to return.
     * @return A collection of {@link Class}es from the named types, or an empty
     * list if names is null.
     */
    public static <T> Collection<Class<T>> convertTypes(final Collection<String> names, final Class<T> type) {
        Objects.requireNonNull(names);
        Objects.requireNonNull(type);

        List<Class<T>> types = new ArrayList<>();

        for (String name : names)
            types.add(convertType(name, type));

        return types;
    }

    /**
     * @see #convertType(String, Class)
     *
     * @param name The name of the class to find.
     * @return The Java {@link Class} of this type if one is found.
     * @throws RuntimeException If the classpath does not contain such a class or if the class
     * found is is not the type required.
     */
    public static Class<Object> convertType(final String name) {
        return convertType(name, Object.class);
    }

    /**
     * Convert a class string to an actual Java class.
     *
     * @param name The name of the class to find.
     * @param type The type of class required.
     * @param <T> The type of class required.
     * @return The Java {@link Class} of this type if one is found.
     * @throws RuntimeException If the classpath does not contain such a class or if the class
     * found is is not the type required.
     */
    public static <T> Class<T> convertType(final String name, final Class<T> type) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(type);

        try {
            Class<?> typeFound = Class.forName(name);

            if (!type.isAssignableFrom(typeFound))
                throw new RuntimeException("Type found does not match type requires.");

            return (Class<T>)typeFound;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Add data for all modules found in the packages specified.
     *
     * @param reference Class is used as a reference for the root package to search.
     * @return A collection of classes found in under the package of this class.
     */
    public static Collection<Class<?>> getClasses(Class<?> reference) {
        return getClasses(reference.getPackage());
    }

    /**
     * @param pkge The package to find all classes and load from.
     * @return A collection of classes found under this package.
     */
    public static Collection<Class<?>> getClasses(Package pkge) {
        try {
            return getClasses(pkge.getName());
        } catch (IOException ex) {
            logger.error("The package string of this package caused an exception.", ex);
            return List.of();
        }
    }

    /**
     * @param pkge <p>The package name to search through, for example:</p>
     * <code>org.elypia.alexis.commandler</code>
     * @return A collection of classes found under this package.
     */
    public static Collection<Class<?>> getClasses(String pkge) throws IOException {
        Collection<Class<?>> list = new ArrayList<>();

        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        String path = pkge.replace('.', File.separatorChar);

        loader.getResources(path).asIterator().forEachRemaining((url) -> {
            try {
                URI uri = url.toURI();
                logger.debug("Getting classes for URI: {}", uri);

                File dir = new File(uri);
                addClasses(dir, pkge, list);
            } catch (URISyntaxException ex) {
                logger.error("Package returned bad URL.", ex);
            }
        });

        return list;
    }

    /**
     * Add all classes in the specified package recursivley to the list
     * provided.
     *
     * @param dir The directory we're loading from.
     * @param packageName The name of the package we're loading.
     * @param list The list to add all classes that are found.
     */
    private static void addClasses(File dir, String packageName, Collection<Class<?>> list) {
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
                    Class type = Class.forName(packageName + "." + fileName);
                    int modifiers = type.getModifiers();

                    if (Modifier.isInterface(modifiers) || Modifier.isAbstract(modifiers))
                        continue;

                    list.add(type);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

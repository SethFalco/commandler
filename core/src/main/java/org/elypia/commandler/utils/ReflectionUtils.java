package org.elypia.commandler.utils;

import org.slf4j.*;

import java.io.*;
import java.lang.reflect.Modifier;
import java.net.*;
import java.util.*;

public final class ReflectionUtils {

    private static final Logger logger = LoggerFactory.getLogger(ReflectionUtils.class);

    private ReflectionUtils() {
        // Do nothing
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
     *
     * @param pkge
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
     * <p>The package name to search through, for example:</p>
     * <code>org.elypia.alexis.commandler</code>

     * @param pkge The package to search.
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
     * Add all classes in the specified package recursivley.
     *
     * @param dir The directory we're loading from.
     * @param packageName The name of the package we're loading.
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

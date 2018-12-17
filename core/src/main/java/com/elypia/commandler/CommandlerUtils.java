package com.elypia.commandler;

import org.slf4j.*;

import java.io.*;
import java.net.URISyntaxException;
import java.util.*;

public final class CommandlerUtils {

    private static final Logger logger = LoggerFactory.getLogger(CommandlerUtils.class);

    public static <T> T getLastElement(Iterator<T> iter) {
        T item = null;

        while (iter.hasNext())
            item = iter.next();

        return item;
    }

    /**
     * Add data for all modules found in the packages specified.
     *
     * @param packageName A list of packages to load modules from.
     */
    public static <T extends Class> List<T> getClasses(String packageName, T clazz) {
        List<T> list = new ArrayList<>();

        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        String path = packageName.replace('.', File.separatorChar);

        try {
            loader.getResources(path).asIterator().forEachRemaining((url) -> {
                try {
                    File dir = new File(url.toURI());
                    addClasses(dir, packageName, clazz, list);
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
     * @param packageName The id of the package we're loading.
     */
    private static <T extends Class> void addClasses(File dir, String packageName, T clazz, List<T> list) {
        File[] files = dir.listFiles();

        if (files == null)
            return;

        for (File file : files) {
            String fileFullName = file.getName();

            if (file.isDirectory())
                addClasses(file, packageName + "." + fileFullName, clazz, list);

            else if (fileFullName.endsWith(".class")) {
                String fileName = fileFullName.substring(0, fileFullName.length() - 6);

                try {
                    String className = packageName + "." + fileName;
                    Class<?> clazz2 = Class.forName(className);

                    if (clazz.isAssignableFrom(clazz2))
                        list.add((T)clazz2.asSubclass(clazz));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}


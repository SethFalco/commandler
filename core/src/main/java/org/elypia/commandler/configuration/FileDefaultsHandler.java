package org.elypia.commandler.configuration;

import org.apache.commons.configuration2.builder.DefaultParametersHandler;
import org.apache.commons.configuration2.builder.fluent.FileBasedBuilderParameters;

import java.nio.charset.StandardCharsets;

/**
 * Load all files as {@link StandardCharsets#UTF_8}.
 */
public class FileDefaultsHandler implements DefaultParametersHandler<FileBasedBuilderParameters> {

    private static final String UTF_8 = StandardCharsets.UTF_8.name();

    @Override
    public void initializeDefaults(FileBasedBuilderParameters parameters) {
        parameters.setEncoding(UTF_8);
    }
}

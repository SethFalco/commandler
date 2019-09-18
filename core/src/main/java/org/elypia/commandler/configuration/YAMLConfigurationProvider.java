package org.elypia.commandler.configuration;

import org.apache.commons.configuration2.YAMLConfiguration;
import org.apache.commons.configuration2.builder.*;
import org.apache.commons.configuration2.builder.combined.BaseConfigurationBuilderProvider;

import java.util.List;

public class YAMLConfigurationProvider extends BaseConfigurationBuilderProvider {

    public YAMLConfigurationProvider() {
        super(
            FileBasedConfigurationBuilder.class.getName(),
            ReloadingFileBasedConfigurationBuilder.class.getName(),
            YAMLConfiguration.class.getName(),
            List.of(FileBasedBuilderParametersImpl.class.getName())
        );
    }
}

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

package org.elypia.commandler.configuration;

import org.apache.commons.configuration2.ImmutableHierarchicalConfiguration;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.elypia.commandler.Commandler;
import org.junit.jupiter.api.Test;
import org.slf4j.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This is using the internally managed and contained default configuration
 * for {@link Commandler}, this tests the general configuration as
 * well as well as our particular configuration files.
 *
 * @author seth@elypia.org (Syed Shah)
 */
public class CommanderConfigurationTest {

    private static final Logger logger = LoggerFactory.getLogger(CommanderConfigurationTest.class);

    /** Verify we can load the configuration without a fatal exception. */
    @Test
    public void testBasicLoad() {
        assertDoesNotThrow(() -> {
            CommandlerConfiguration config = new CommandlerConfiguration();
            config.getProperties().list(System.out);
        });
    }

    /**
     * Verify if the XML configuration has the property keys correctly formatted.
     *
     * @throws ConfigurationException
     */
    @Test
    public void testXmlPath() throws ConfigurationException {
        CommandlerConfiguration config = new CommandlerConfiguration();
        List<String> list = config.getList(String.class, "commandler.controller.type");

        int expected = 1;
        int actual = list.size();

        assertEquals(expected, actual);
    }

    @Test
    public void testIterator() throws ConfigurationException {
        CommandlerConfiguration config = new CommandlerConfiguration();
        List<ImmutableHierarchicalConfiguration> controllers = config.getConfiguration().immutableConfigurationsAt("commandler.controller");

        int expected = 1;
        int actual = controllers.size();

        assertEquals(expected, actual);
    }

    @Test
    public void testIteratorValues() throws ConfigurationException {
        CommandlerConfiguration config = new CommandlerConfiguration();
        List<ImmutableHierarchicalConfiguration> controllers = config.getConfiguration().immutableConfigurationsAt("commandler.controller");
        ImmutableHierarchicalConfiguration controller = controllers.get(0);

        assertAll("Check all values are correct.",
            () -> assertEquals("Help", controller.getString("name")),
            () -> assertEquals("Guidance", controller.getString("group"))
        );
    }

    /**
     * Verify that the value we try to get matches what was put in the XML file.
     *
     * @throws ConfigurationException
     */
    @Test
    public void testXmlValue() throws ConfigurationException {
        CommandlerConfiguration configuration = new CommandlerConfiguration();

        String expected = "Offer generic help through all the modules in this bot.";
        String actual = configuration.getString("commandler.controller(0).description");

        assertEquals(expected, actual);
    }
}

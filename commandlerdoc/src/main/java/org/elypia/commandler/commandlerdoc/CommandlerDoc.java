/*
 * Copyright 2019-2020 Elypia CIC
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

package org.elypia.commandler.commandlerdoc;

import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.elypia.commandler.CommandlerExtension;
import org.elypia.commandler.commandlerdoc.markdown.MarkdownExporter;
import org.elypia.commandler.commandlerdoc.models.ExportableData;
import org.elypia.commandler.metadata.*;
import org.slf4j.*;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * A class to manage exportable metadata. This includes
 * actual command data for the command handler, and possibly metadata
 * for the application, or configuration for a service to work with.
 *
 * This will only export public metadata. For example where
 * {@link MetaController#isPublic()} and {@link MetaCommand#isPublic} is true.
 *
 * @author seth@elypia.org (Seth Falco)
 */
public class CommandlerDoc {

    private static final Logger logger = LoggerFactory.getLogger(CommandlerDoc.class);

    private List<MetaController> modules;

    public CommandlerDoc() throws IOException {
        this(List.of(Locale.getDefault()));
    }

    public CommandlerDoc(List<Locale> locales) throws IOException {
        this(locales, new MarkdownExporter());
    }

    public CommandlerDoc(List<Locale> locales, Exporter exporter) throws IOException {
        this(BeanProvider.getContextualReference(CommandlerExtension.class, false), locales, exporter);
    }

    public CommandlerDoc(CommandlerExtension extension, List<Locale> locales, Exporter exporter) throws IOException {
        this(extension.getMetaControllers(), locales, exporter);
    }

    public CommandlerDoc(MetaController[] modules, List<Locale> locales, Exporter exporter) throws IOException {
        this(List.of(modules), locales, exporter);
    }

    public CommandlerDoc(Collection<MetaController> controllers, List<Locale> locales, Exporter exporter) throws IOException {
        this.modules = controllers.stream()
            .filter(MetaController::isPublic)
            .sorted()
            .collect(Collectors.toUnmodifiableList());

        ExportableData data = new DocTransformer().transform(this.modules, locales);
        exporter.export(data);
    }
}

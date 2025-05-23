/*
 * Copyright 2019-2025 Seth Falco and Commandler Contributors
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

package fun.falco.commandler.commandlerdoc;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.apache.deltaspike.core.api.provider.BeanProvider;

import fun.falco.commandler.CommandlerExtension;
import fun.falco.commandler.commandlerdoc.markdown.MarkdownExporter;
import fun.falco.commandler.commandlerdoc.models.ExportableData;
import fun.falco.commandler.metadata.MetaCommand;
import fun.falco.commandler.metadata.MetaController;

/**
 * Manages exportable metadata. Includes actual command data for the command
 * handler, and possibly metadata for the application, or configuration for a
 * service to work with.
 *
 * <p>This will only export public metadata. For example where
 * {@link MetaController#isPublic()} and {@link MetaCommand#isPublic} is
 * true.</p>
 *
 * @author seth@falco.fun (Seth Falco)
 */
public class CommandlerDoc {

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

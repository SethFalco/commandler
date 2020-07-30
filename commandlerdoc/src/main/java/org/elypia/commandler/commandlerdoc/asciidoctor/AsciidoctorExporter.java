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

package org.elypia.commandler.commandlerdoc.asciidoctor;

import org.elypia.commandler.commandlerdoc.Exporter;
import org.elypia.commandler.commandlerdoc.models.*;
import org.slf4j.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @author seth@elypia.org (Seth Falco)
 * @since 4.0.2
 */
public class AsciidoctorExporter implements Exporter {

    private static final Logger logger = LoggerFactory.getLogger(AsciidoctorExporter.class);

    @Override
    public void export(ExportableData data) throws IOException {
        Map<Locale, List<ExportableController>> controllers = data.getControllers();
        Map<File, String> adocFiles = exportBundled(controllers);

        for (Map.Entry<File, String> entry : adocFiles.entrySet()) {
            File file = entry.getKey();

            File parent = file.getParentFile();

            if (parent != null && parent.mkdirs())
                logger.info("Created directory at path: {}", parent.getAbsolutePath());

            try (FileWriter writer = new FileWriter(file, StandardCharsets.UTF_8)){
                writer.write(entry.getValue());
            }
        }
    }

    /**
     * Export all controllers into a single document per language.
     *
     * @return A map of file to write mapped to the content to write against them.
     */
    private Map<File, String> exportBundled(Map<Locale, List<ExportableController>> controllers) {
        Map<File, String> adocFiles = new HashMap<>();

        controllers.forEach((locale, exportableControllers) -> {
            StringJoiner joiner = new StringJoiner("\n\n");

            for (ExportableController exportableController : exportableControllers) {
                String serialized = serializeController(exportableController);
                joiner.add(serialized);
            }

            String filename = "index.adoc";
            String filepath = "commandlerdoc/" + locale.toLanguageTag() + File.separator + filename;
            File output = new File(filepath);
            adocFiles.put(output, joiner.toString());
        });

        return adocFiles;
    }

    /**
     * Export controllers individually as their own documents.
     *
     * @return A map of file to write mapped to the content to write against them.
     */
    private Map<File, String> exportSeperated(Map<Locale, List<ExportableController>> controllers) {
        Map<File, String> adocFiles = new HashMap<>();

        controllers.forEach((locale, exportableControllers) -> {
            for (ExportableController exportableController : exportableControllers) {
                String serialized = serializeController(exportableController);
                String filename = exportableController.getName().toLowerCase().replace(" ", "-") + ".adoc";
                String filepath = "commandlerdoc/" + locale.toLanguageTag() + File.separator + filename;
                File output = new File(filepath);
                adocFiles.put(output, serialized);
            }
        });

        return adocFiles;
    }

    private String serializeController(ExportableController controller) {
        StringBuilder builder = new StringBuilder();

        builder.append("= ").append(controller.getName()).append("\n\n")
            .append(controller.getGroup()).append("\n\n")
            .append(controller.getDescription()).append("\n\n")
            .append("== Commands\n");

        controller.getCommands().forEach((command) -> {
            builder.append("=== ").append(command.getName()).append("\n\n")
                .append(command.getDescription()).append("\n\n");

            builder.append("==== Parameters\n\n");

            command.getParams().forEach((param) -> {
                builder.append("===== ").append(param.getName()).append("\n\n")
                    .append(param.getDescription()).append("\n\n");
            });
        });

        return builder.toString();
    }
}

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

package org.elypia.commandler.commandlerdoc.markdown;

import org.elypia.commandler.commandlerdoc.Exporter;
import org.elypia.commandler.commandlerdoc.models.*;

import java.io.*;
import java.util.*;

/**
 * @author seth@elypia.org (Seth Falco)
 * @since 4.0.2
 */
public class MarkdownExporter implements Exporter {

    @Override
    public void export(ExportableData data) {
        Map<Locale, List<ExportableController>> controllers = data.getControllers();

        controllers.forEach((locale, exportableControllers) -> {
            File file = new File("./commandlerdoc/" + locale.toLanguageTag() + "/");
            file.mkdirs();

            for (ExportableController exportableController : exportableControllers) {
                String name = exportableController.getName().toLowerCase().replace(" ", "-");
                StringBuilder builder = new StringBuilder();

                builder.append("# " + exportableController.getName() + "\n");
                builder.append(exportableController.getGroup() + "  \n");
                builder.append(exportableController.getDescription() + "  \n");
                builder.append("## " + "Commands\n");

                exportableController.getCommands().forEach((command) -> {
                    builder.append("### " + command.getName() + "\n");
                    builder.append(command.getDescription() + "\n");

                    builder.append("#### " + "Parameters\n");

                    command.getParams().forEach((param) -> {
                        builder.append("##### " + param.getName() + "\n");
                        builder.append(param.getDescription() + "\n");
                    });
                });

                try (FileWriter writer = new FileWriter("./commandlerdoc/" + locale.toLanguageTag() + "/" + name + ".md")) {
                    writer.write(builder.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}

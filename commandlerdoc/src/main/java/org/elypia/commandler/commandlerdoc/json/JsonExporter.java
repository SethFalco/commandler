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

package org.elypia.commandler.commandlerdoc.json;

import org.elypia.commandler.commandlerdoc.Exporter;
import org.elypia.commandler.commandlerdoc.json.deserializers.ExportableCommandSerializer;
import org.elypia.commandler.commandlerdoc.json.deserializers.ExportableControllerSerializer;
import org.elypia.commandler.commandlerdoc.json.deserializers.ExportableDataSerializer;
import org.elypia.commandler.commandlerdoc.json.deserializers.ExportableParamSerializer;
import org.elypia.commandler.commandlerdoc.models.ExportableCommand;
import org.elypia.commandler.commandlerdoc.models.ExportableController;
import org.elypia.commandler.commandlerdoc.models.ExportableData;
import org.elypia.commandler.commandlerdoc.models.ExportableParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author seth@falco.fun (Seth Falco)
 * @since 4.0.2
 */
public class JsonExporter implements Exporter {

    private static final Logger logger = LoggerFactory.getLogger(JsonExporter.class);

    private final Gson gson;

    public JsonExporter() {
        this.gson = new GsonBuilder()
            .registerTypeAdapter(ExportableData.class, new ExportableDataSerializer())
            .registerTypeAdapter(ExportableController.class, new ExportableControllerSerializer())
            .registerTypeAdapter(ExportableCommand.class, new ExportableCommandSerializer())
            .registerTypeAdapter(ExportableParameter.class, new ExportableParamSerializer())
            .create();
    }

    @Override
    public void export(final ExportableData data) {
        final String json = gson.toJson(data);
        logger.info("Exported JSON: {}", json);
    }
}

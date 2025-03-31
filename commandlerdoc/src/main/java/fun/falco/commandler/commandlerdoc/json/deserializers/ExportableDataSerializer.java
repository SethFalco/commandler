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

package fun.falco.commandler.commandlerdoc.json.deserializers;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import fun.falco.commandler.commandlerdoc.models.ExportableController;
import fun.falco.commandler.commandlerdoc.models.ExportableData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * @author seth@falco.fun (Seth Falco)
 * @since 4.0.2
 */
public class ExportableDataSerializer implements JsonSerializer<ExportableData> {

    private static final Logger logger = LoggerFactory.getLogger(ExportableDataSerializer.class);

    @Override
    public JsonElement serialize(ExportableData data, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        Map<Locale, List<ExportableController>> controllers = data.getControllers();
        logger.info("Exporting data for {} locales.", controllers.size());

        controllers.forEach((locale, exportableControllers) -> {
            JsonArray array = new JsonArray();

            for (ExportableController ec : exportableControllers) {
                array.add(context.serialize(ec));
            }

            object.add(locale.toLanguageTag(), array);
        });

        return object;
    }
}

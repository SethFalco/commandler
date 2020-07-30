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

package org.elypia.commandler.commandlerdoc.json.deserializers;

import com.google.gson.*;
import org.elypia.commandler.commandlerdoc.models.*;
import org.slf4j.*;

import java.lang.reflect.Type;
import java.util.*;

/**
 * @author seth@elypia.org (Seth Falco)
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

            for (ExportableController ec : exportableControllers)
                array.add(context.serialize(ec));

            object.add(locale.toLanguageTag(), array);
        });

        return object;
    }
}

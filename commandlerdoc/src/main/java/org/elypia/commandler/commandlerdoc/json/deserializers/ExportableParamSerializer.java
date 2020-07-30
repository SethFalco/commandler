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
import org.elypia.commandler.commandlerdoc.models.ExportableParameter;

import java.lang.reflect.Type;

/**
 * @author seth@elypia.org (Seth Falco)
 */
public class ExportableParamSerializer implements JsonSerializer<ExportableParameter> {

    @Override
    public JsonElement serialize(ExportableParameter param, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        object.addProperty("locale", param.getLocale().toLanguageTag());
        object.addProperty("name", param.getName());
        object.addProperty("description", param.getDescription());
        return object;
    }
}

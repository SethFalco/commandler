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

package org.elypia.commandler.doc.deserializers;

import com.google.gson.*;
import org.elypia.commandler.metadata.*;

import java.lang.reflect.Type;

/**
 * @author seth@elypia.org (Seth Falco)
 */
public class MetaControlSerializer implements JsonSerializer<MetaCommand> {

    @Override
    public JsonElement serialize(MetaCommand src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        object.addProperty("name", src.getName());
        object.addProperty("description", src.getDescription());
        object.addProperty("minParams", src.getMinParams());
        object.addProperty("maxParams", src.getMaxParams());
        object.addProperty("default", src.isDefault());
        object.addProperty("static", src.isStatic());

        JsonArray params = new JsonArray();
        for (MetaParam metaParam : src.getMetaParams())
            params.add(context.serialize(metaParam));
        object.add("params", params);

        return object;
    }
}

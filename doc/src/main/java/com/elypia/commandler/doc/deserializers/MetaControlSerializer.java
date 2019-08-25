package com.elypia.commandler.doc.deserializers;

import com.elypia.commandler.metadata.*;
import com.google.gson.*;

import java.lang.reflect.Type;

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

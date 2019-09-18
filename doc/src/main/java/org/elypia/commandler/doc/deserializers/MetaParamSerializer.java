package org.elypia.commandler.doc.deserializers;

import com.google.gson.*;
import org.elypia.commandler.metadata.MetaParam;

import java.lang.reflect.Type;

public class MetaParamSerializer implements JsonSerializer<MetaParam> {

    @Override
    public JsonElement serialize(MetaParam src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        object.addProperty("name", src.getName());
        object.addProperty("description", src.getDescription());
        object.addProperty("list", src.isList());
        object.addProperty("required", src.isRequired());

        return object;
    }
}

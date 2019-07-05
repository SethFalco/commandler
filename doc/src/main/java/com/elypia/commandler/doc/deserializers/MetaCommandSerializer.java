package com.elypia.commandler.doc.deserializers;

import com.elypia.commandler.metadata.data.*;
import com.google.gson.*;

import java.lang.reflect.Type;

public class MetaCommandSerializer implements JsonSerializer<MetaCommand> {

    @Override
    public JsonElement serialize(MetaCommand src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        object.addProperty("name", src.getName());
        object.addProperty("help", src.getHelp());
        object.addProperty("minParams", src.getMinParams());
        object.addProperty("maxParams", src.getMaxParams());
        object.addProperty("default", src.isDefault());
        object.addProperty("static", src.isStatic());

        JsonArray aliases = new JsonArray();
        for (String alias : src.getAliases())
            aliases.add(alias);
        object.add("aliases", aliases);

        JsonArray params = new JsonArray();
        for (MetaParam param : src.getParams())
            params.add(context.serialize(param));
        object.add("params", params);

        return object;
    }
}

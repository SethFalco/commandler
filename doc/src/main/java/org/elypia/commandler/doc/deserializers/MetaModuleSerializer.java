package org.elypia.commandler.doc.deserializers;

import com.google.gson.*;
import org.elypia.commandler.metadata.*;

import java.lang.reflect.Type;

public class MetaModuleSerializer implements JsonSerializer<MetaController> {

    @Override
    public JsonElement serialize(MetaController src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        object.addProperty("group", src.getGroup());
        object.addProperty("name", src.getName());
        object.addProperty("description", src.getDescription());

        JsonArray commands = new JsonArray();
        for (MetaCommand metaCommand : src.getPublicCommands())
            commands.add(context.serialize(metaCommand));
        object.add("commands", commands);

        return object;
    }
}

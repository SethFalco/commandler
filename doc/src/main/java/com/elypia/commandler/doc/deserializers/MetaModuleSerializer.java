package com.elypia.commandler.doc.deserializers;

import com.elypia.commandler.metadata.*;
import com.google.gson.*;

import java.lang.reflect.Type;

public class MetaModuleSerializer implements JsonSerializer<MetaModule> {

    @Override
    public JsonElement serialize(MetaModule src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        object.addProperty("group", src.getGroup());
        object.addProperty("name", src.getName());
        object.addProperty("help", src.getHelp());

        JsonArray aliases = new JsonArray();
        for (String alias : src.getAliases())
            aliases.add(alias);
        object.add("aliases", aliases);

        JsonArray commands = new JsonArray();
        for (MetaCommand command : src.getPublicCommands())
            commands.add(context.serialize(command));
        object.add("commands", commands);

        return object;
    }
}

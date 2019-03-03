package com.elypia.commandler.export.deserializers;

import com.elypia.commandler.metadata.Context;
import com.google.gson.*;

import java.lang.reflect.Type;

public class ModulesContextSerializer implements JsonSerializer<Context> {

    @Override
    public JsonElement serialize(Context src, Type typeOfSrc, JsonSerializationContext context) {
//        JsonObject object = new JsonObject();
//        JsonArray modules = new JsonArray();
//
//        src.getModuleBuilders(false).forEach(moduleData -> {
//            JsonObject module = new JsonObject();
//
//            module.addProperty("id", moduleAnno.name());
//            module.addProperty("group", moduleAnno.group());
//
//            addMutual(module, moduleAnno.aliases(), moduleAnno.help());
//
//            JsonArray commands = new JsonArray();
//            moduleData.getPublicCommands().forEach(commandData -> {
//                JsonObject command = new JsonObject();
//
//                Command commandAnno = commandData.getAnnotation();
//                command.addProperty("id", commandAnno.name());
//
//                addMutual(command, commandAnno.aliases(), commandAnno.help());
//
//                JsonArray params = new JsonArray();
//                commandData.getInputParams().forEach(paramData -> {
//                    JsonObject param = new JsonObject();
//
//                    Param paramAnno = paramData.getAnnotation();
//                    param.addProperty("id", paramAnno.name());
//                    param.addProperty("value", paramAnno.value());
//
//                    params.add(param);
//                });
//
//                command.add("parameters", params);
//                commands.add(command);
//            });
//
//            module.add("commands", commands);
//            modules.add(module);
//        });
//
//        object.add("modules", modules);
//
//        JsonArray groups = new JsonArray();
//        src.getGroups(false).forEach((name, groupModules) -> {
//            JsonObject group = new JsonObject();
//
//            group.addProperty("name", name);
//
//            JsonArray moduleIds = new JsonArray();
//            groupModules.forEach(o -> moduleIds.add(o.getAnnotation().name()));
//            group.add("modules", moduleIds);
//
//            groups.add(group);
//        });
//
//        object.add("groups", groups);
//
//        return object;
        return null;
    }

    private void addMutual(JsonObject src, String[] aliases, String help) {
        JsonArray jsonAliases = new JsonArray();

        for (String alias : aliases)
            jsonAliases.add(alias);

        src.add("aliases", jsonAliases);
        src.addProperty("value", help);
    }
}

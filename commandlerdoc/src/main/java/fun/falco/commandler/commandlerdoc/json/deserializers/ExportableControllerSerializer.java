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

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import fun.falco.commandler.commandlerdoc.models.ExportableCommand;
import fun.falco.commandler.commandlerdoc.models.ExportableController;

/**
 * @author seth@falco.fun (Seth Falco)
 */
public class ExportableControllerSerializer implements JsonSerializer<ExportableController> {

    @Override
    public JsonElement serialize(ExportableController controller, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        object.addProperty("locale", controller.getLocale().toLanguageTag());
        object.addProperty("name", controller.getName());
        object.addProperty("description", controller.getDescription());
        object.addProperty("group", controller.getGroup());

        JsonArray commands = new JsonArray();

        for (ExportableCommand exportableCommand : controller.getCommands()) {
            commands.add(context.serialize(exportableCommand));
        }

        object.add("commands", commands);
        return object;
    }
}

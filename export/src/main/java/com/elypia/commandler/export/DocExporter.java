package com.elypia.commandler.export;

import com.elypia.commandler.ModulesContext;
import com.elypia.commandler.export.deserializers.ModulesContextSerializer;
import com.google.gson.*;

public class DocExporter {

    private ModulesContext context;

    public DocExporter() {

    }

    public String toJson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(ModulesContext.class, new ModulesContextSerializer());
        Gson gson = gsonBuilder.create();

        return gson.toJson(context);
    }

    public ModulesContext getContext() {
        return context;
    }

    public DocExporter setContext(ModulesContext context) {
        this.context = context;
        return this;
    }
}

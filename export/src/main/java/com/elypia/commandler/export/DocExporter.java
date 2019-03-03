package com.elypia.commandler.export;

import com.elypia.commandler.export.deserializers.ModulesContextSerializer;
import com.elypia.commandler.metadata.Context;
import com.google.gson.*;

public class DocExporter {

    private Context context;

    public DocExporter() {

    }

    public String toJson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Context.class, new ModulesContextSerializer());
        Gson gson = gsonBuilder.create();

        return gson.toJson(context);
    }

    public Context getContext() {
        return context;
    }

    public DocExporter setContext(Context context) {
        this.context = context;
        return this;
    }
}

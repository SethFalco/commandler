package com.elypia.commandler.doc;

import com.elypia.commandler.Context;
import com.elypia.commandler.doc.deserializers.*;
import com.elypia.commandler.metadata.*;
import com.google.gson.*;
import org.slf4j.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * A class to manage exportable metadata. This includes
 * actual command data for the command handler, and possibly metadata
 * for the application, or configuration for a service to work with.
 *
 * This will only export public metadata. For example where
 * {@link MetaModule#isPublic()} and {@link MetaCommand#isPublic} is true.
 */
public class CommandlerDoc {

    private static final Logger logger = LoggerFactory.getLogger(CommandlerDoc.class);

    private static final Gson gson = new GsonBuilder()
        .registerTypeAdapter(MetaModule.class, new MetaModuleSerializer())
        .registerTypeAdapter(MetaCommand.class, new MetaCommandSerializer())
        .registerTypeAdapter(MetaParam.class, new MetaParamSerializer())
        .create();

    private List<MetaModule> modules;

    public CommandlerDoc(Context context) {
        this(context.getModules());
    }

    public CommandlerDoc(MetaModule... modules) {
        this(List.of(modules));
    }

    public CommandlerDoc(List<MetaModule> modules) {
        this.modules = modules.stream()
            .filter(MetaModule::isPublic)
            .sorted()
            .collect(Collectors.toUnmodifiableList());
    }

    public String toJson() {
        String json = gson.toJson(modules);
        logger.info("Exported JSON: {}", json);
        return json;
    }
}

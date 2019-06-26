package com.elypia.commandler.adapters;

import com.elypia.commandler.*;
import com.elypia.commandler.annotations.Adapter;
import com.elypia.commandler.interfaces.ParamAdapter;
import com.elypia.commandler.metadata.data.*;

import javax.inject.*;

/** Take a command handler/module as a parameter. */
@Singleton
@Adapter(MetaModule.class)
public class MetaModuleAdapter implements ParamAdapter<MetaModule> {

    private Context context;

    @Inject
    public MetaModuleAdapter(Context context) {
        this.context = context;
    }

    @Override
    public MetaModule adapt(String input, Class<? extends MetaModule> type, MetaParam param, CommandlerEvent<?> event) {
        return context.getModule(input);
    }
}

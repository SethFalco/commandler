package com.elypia.commandler.adapters;

import com.elypia.commandler.annotations.Compatible;
import com.elypia.commandler.interfaces.Adapter;
import com.elypia.commandler.metadata.Context;
import com.elypia.commandler.metadata.data.*;

import javax.inject.*;

/** Take a command handler/module as a parameter. */
@Singleton
@Compatible(MetaModule.class)
public class MetaModuleAdapter implements Adapter<MetaModule> {

    private Context context;

    @Inject
    public MetaModuleAdapter(Context context) {
        this.context = context;
    }

    @Override
    public MetaModule adapt(String input, Class<? extends MetaModule> type, ParamData param) {
        return context.getModule(input);
    }
}

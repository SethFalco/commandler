package com.elypia.commandler.adapters;

import com.elypia.commandler.Utils;
import com.elypia.commandler.annotations.Compatible;
import com.elypia.commandler.interfaces.Adapter;
import com.elypia.commandler.metadata.Context;
import com.elypia.commandler.metadata.data.*;

import javax.inject.*;

/** Take a command handler/module as a parameter. */
@Singleton
@Compatible(MetaCommand.class)
public class MetaCommandAdapter implements Adapter<MetaCommand> {

    private Context context;

    @Inject
    public MetaCommandAdapter(Context context) {
        this.context = context;
    }

    @Override
    public MetaCommand adapt(String input, Class<? extends MetaCommand> type, ParamData param) {
        String[] args = Utils.splitSpaces(input);

        if (args.length != 2)
            return null;

        MetaModule module = context.getModule(args[0]);

        if (module == null)
            return null;

        for (MetaCommand command : module.getCommands()) {
            if (command.performed(args[1]))
                return command;
        }

        return null;
    }
}

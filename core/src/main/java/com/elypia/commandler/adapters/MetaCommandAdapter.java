package com.elypia.commandler.adapters;

import com.elypia.commandler.*;
import com.elypia.commandler.annotations.Adapter;
import com.elypia.commandler.interfaces.ParamAdapter;
import com.elypia.commandler.metadata.data.*;
import com.elypia.commandler.utils.CommandlerUtils;

import javax.inject.*;

/** Take a command handler/module as a parameter. */
@Singleton
@Adapter(MetaCommand.class)
public class MetaCommandAdapter implements ParamAdapter<MetaCommand> {

    private Context context;

    @Inject
    public MetaCommandAdapter(Context context) {
        this.context = context;
    }

    @Override
    public MetaCommand adapt(String input, Class<? extends MetaCommand> type, MetaParam param, CommandlerEvent<?> event) {
        String[] args = CommandlerUtils.splitSpaces(input);

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

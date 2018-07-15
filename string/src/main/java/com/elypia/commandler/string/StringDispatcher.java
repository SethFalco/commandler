package com.elypia.commandler.string;

import com.elypia.commandler.*;
import com.elypia.commandler.events.CommandEvent;
import com.elypia.commandler.metadata.AbstractMetaCommand;

import java.lang.reflect.InvocationTargetException;

public class StringDispatcher extends Dispatcher<Void, String> {

    @Override
    public String processEvent(Void event) {
        throw new UnsupportedOperationException(StringCommandler.EVENT_PROVIDED);
    }

    @Override
    public String processEvent(Void v, String content) {
        CommandInput input = confiler.processEvent(commandler, v, content);

        if (input == null)
            return null;

        CommandEvent<Void, String> event = new CommandEvent<>(commandler, v);

        if (!commandler.getRootAlises().contains(input.getModule().toLowerCase()) || !input.normalize())
            return null;

        AbstractMetaCommand metaCommand = event.getMetaCommand();

        if (!validator.validateCommand(event, metaCommand))
            return null;

        Object[] params = parser.parseParameters(event, metaCommand);

        if (params == null || !validator.validateParams(event, metaCommand, params))
            return null;

        if (!input.getCommand().equalsIgnoreCase("help") && !input.getMetaModule().getHandler().isEnabled()) {
            confiler.getMisuseListener().moduleDisabled(event);
            return null;
        }

        try {
            Object message = metaCommand.getMethod().invoke(metaCommand.getHandler(), params);

            if (message != null) {
                String m = builder.buildMessage(event, v);
                event.reply(m);
                return m;
            }
        } catch (IllegalAccessException | InvocationTargetException ex) {
            confiler.getMisuseListener().exceptionFailure(ex);
            ex.printStackTrace();
        }

        return null;
    }
}

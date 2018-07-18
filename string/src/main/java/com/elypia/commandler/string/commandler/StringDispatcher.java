package com.elypia.commandler.string.commandler;

import com.elypia.commandler.*;
import com.elypia.commandler.CommandEvent;
import com.elypia.commandler.metadata.AbstractMetaCommand;
import com.elypia.commandler.string.client.*;

import java.lang.reflect.InvocationTargetException;

public class StringDispatcher extends Dispatcher<StringClient, StringEvent, String> implements StringListener {

    protected StringDispatcher(Commandler<StringClient, StringEvent, String> commandler) {
        super(commandler);
        client.addListener(this);
    }

    @Override
    public void onStringEvent(StringEvent event) {
        processEvent(event);
    }

    @Override
    public String processEvent(StringEvent event) {
        return processEvent(event, event.getContent());
    }

    @Override
    public String processEvent(StringEvent v, String content) {
        CommandInput<StringClient, StringEvent, String> input = confiler.processEvent(commandler, v, content);

        if (input == null)
            return null;

        CommandEvent<StringClient, StringEvent, String> event = new StringCommand(commandler, v, input);

        if (!commandler.getRoots().containsKey(input.getModule().toLowerCase()) || !input.normalize())
            return null;

        AbstractMetaCommand metaCommand = event.getInput().getMetaCommand();

        if (!commandler.getValidator().validateCommand(event, metaCommand))
            return null;

        Object[] params = commandler.getParser().parseParameters(event, metaCommand);

        if (params == null || !commandler.getValidator().validateParams(event, metaCommand, params))
            return null;

//        if (!input.getCommand().equalsIgnoreCase("help") && !input.getMetaModule().getHandler().isEnabled()) {
//            confiler.getMisuseListener().moduleDisabled(event);
//            return null;
//        }

        try {
            Object message = metaCommand.getMethod().invoke(metaCommand.getHandler(), params);

            if (message != null)
                return commandler.getBuilder().buildMessage(event, message);
        } catch (IllegalAccessException | InvocationTargetException ex) {
            confiler.getMisuseListener().exceptionFailure(ex);
            ex.printStackTrace();
        }

        return null;
    }
}

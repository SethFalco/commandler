package com.elypia.commandler.string;

import com.elypia.commandler.*;
import com.elypia.commandler.metadata.MetaCommand;
import com.elypia.commandler.string.client.*;
import org.slf4j.*;

public class StringDispatcher extends Dispatcher<StringClient, StringEvent, String> implements StringListener {

    private static final Logger logger = LoggerFactory.getLogger(StringDispatcher.class);

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

        CommandEvent<StringClient, StringEvent, String> event = new StringCommand(commandler, input, v);

        if (!commandler.getRoots().containsKey(input.getModule().toLowerCase()) || !input.normalize(event))
            return event.getError();

        MetaCommand<StringClient, StringEvent, String> metaCommand = event.getInput().getMetaCommand();

        if (!commandler.getValidator().validateCommand(event, metaCommand))
            return event.getError();

        Object[] params = commandler.getParser().processEvent(event, metaCommand);

        if (params == null || !commandler.getValidator().validateParams(event, metaCommand, params))
            return event.getError();

        if (!input.getCommand().equalsIgnoreCase("help") && !input.getMetaModule().getHandler().isEnabled()) {
            confiler.getMisuseListener().onModuleDisabled(event);
            return event.getError();
        }

        try {
            Object message = metaCommand.getMethod().invoke(metaCommand.getHandler(), params);

            if (message != null)
                return commandler.getBuilder().build(event, message);
        } catch (Exception ex) {
            event.invalidate(confiler.getMisuseListener().onExceptionFailure(ex));
            logger.error("An unknown error occured.", ex);
        }

        return null;
    }
}

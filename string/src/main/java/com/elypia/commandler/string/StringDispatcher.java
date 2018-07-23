package com.elypia.commandler.string;

import com.elypia.commandler.*;
import com.elypia.commandler.metadata.MetaCommand;
import com.elypia.commandler.string.client.*;
import org.slf4j.*;

public class StringDispatcher extends Dispatcher<StringClient, StringEvent, String> implements StringListener {

    protected StringDispatcher(StringCommandler commandler) {
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
}

package com.elypia.commandler.string;

import com.elypia.commandler.Dispatcher;
import com.elypia.commandler.string.client.*;

public class StringDispatcher extends Dispatcher<StringClient, StringEvent, String> implements StringListener {

    protected StringDispatcher(StringCommandler commandler) {
        super(commandler);
        client.addListener(this);
    }

    @Override
    public void onStringEvent(StringEvent event) {
        processEvent(event, event.getContent());
    }
}

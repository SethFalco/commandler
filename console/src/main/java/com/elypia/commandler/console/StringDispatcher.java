package com.elypia.commandler.console;

import com.elypia.commandler.console.client.*;

public class StringDispatcher extends Dispatcher<StringClient, StringEvent, String> implements StringListener {

    protected StringDispatcher(StringCommandler commandler) {
        super(commandler);
        client.addListener(this);
    }

    @Override
    public void onStringEvent(StringEvent event) {
        processEvent(event, event.getContent(), true);
    }
}

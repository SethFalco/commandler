package com.elypia.commandler.impl;

import com.elypia.commandler.CommandProcessor;
import com.elypia.commandler.console.client.*;
import com.elypia.commandler.impl.client.*;
import com.elypia.commandler.test.impl.client.*;
import console.client.*;

public class StringDispatcher extends CommandProcessor<StringClient, StringEvent, String> implements StringListener {

    protected StringDispatcher(StringCommandler commandler) {
        super(commandler);
        client.addListener(this);
    }

    @Override
    public void onStringEvent(StringEvent event) {
        processEvent(event, event.getContent(), true);
    }
}

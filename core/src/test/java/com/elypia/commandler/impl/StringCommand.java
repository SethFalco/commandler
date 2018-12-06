package com.elypia.commandler.impl;

import com.elypia.commandler.CommandEvent;
import com.elypia.commandler.console.client.*;
import com.elypia.commandler.impl.client.*;
import com.elypia.commandler.test.impl.client.*;
import console.client.*;

public class StringCommand extends CommandEvent<StringClient, StringEvent, String> {

    private String message;

    public StringCommand(CommandEvent<StringClient, StringEvent, String> event) {
        super(event.getCommandler(), event.getInput(), event.getSource());
        this.message = this.getSource().getContent();
    }

    public String getMessage() {
        return event.getContent();
    }
}

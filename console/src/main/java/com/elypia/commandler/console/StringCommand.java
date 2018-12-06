package com.elypia.commandler.console;

import com.elypia.commandler.CommandEvent;
import com.elypia.commandler.console.client.*;

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

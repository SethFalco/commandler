package com.elypia.commandler.string;

import com.elypia.commandler.*;
import com.elypia.commandler.string.client.*;

public class StringCommand extends CommandEvent<StringClient, StringEvent, String> {

    public StringCommand(CommandEvent<StringClient, StringEvent, String> event) {
        super(event.getCommandler(), event.getInput(), event.getSource());
    }

    @Override
    public String getMessage() {
        return event.getContent();
    }
}

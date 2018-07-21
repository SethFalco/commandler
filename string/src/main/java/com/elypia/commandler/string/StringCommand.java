package com.elypia.commandler.string;

import com.elypia.commandler.*;
import com.elypia.commandler.string.client.*;

public class StringCommand extends CommandEvent<StringClient, StringEvent, String> {

    public StringCommand(Commandler<StringClient, StringEvent, String> commandler, CommandInput<StringClient, StringEvent, String> input, StringEvent event) {
        super(commandler, input, event);
    }

    @Override
    public String reply(Object output) {
        return commandler.getBuilder().build(this, output);
    }

    @Override
    public boolean deleteMessage() {
        throw new UnsupportedOperationException();
    }
}

package com.elypia.commandler.string.commandler;

import com.elypia.commandler.*;
import com.elypia.commandler.string.client.*;

public class StringCommand extends CommandEvent<StringClient, StringEvent, String> {

    public StringCommand(Commandler<StringClient, StringEvent, String> commandler, StringEvent event, CommandInput<StringClient, StringEvent, String> input) {
        super(commandler, event, input);
    }

    @Override
    public <O> String reply(O output) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean deleteMessage() {
        throw new UnsupportedOperationException();
    }
}

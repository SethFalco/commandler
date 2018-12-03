package com.elypia.commandler.string;

import com.elypia.commandler.Handler;
import com.elypia.commandler.annotations.Command;
import com.elypia.commandler.impl.ICommandEvent;
import com.elypia.commandler.string.client.*;

public class StringHandler extends Handler<StringClient, StringEvent, String> {

    @Command(name = "Help", aliases = "help")
    public Object help(ICommandEvent<StringClient, StringEvent, String> event) {
        return super.help(event);
    }
}

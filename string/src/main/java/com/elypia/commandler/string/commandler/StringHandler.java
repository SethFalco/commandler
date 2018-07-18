package com.elypia.commandler.string.commandler;

import com.elypia.commandler.*;
import com.elypia.commandler.annotations.Command;
import com.elypia.commandler.string.client.*;

public class StringHandler extends Handler<StringClient, StringEvent, String> {

    @Override
    @Command(name = "Help", aliases = "help")
    public Object help(CommandEvent<StringClient, StringEvent, String> event) {
        return "helped";
    }
}

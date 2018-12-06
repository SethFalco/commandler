package com.elypia.commandler.impl;

import com.elypia.commandler.Handler;
import com.elypia.commandler.annotations.Command;
import com.elypia.commandler.console.client.*;
import com.elypia.commandler.impl.client.*;
import com.elypia.commandler.test.impl.client.*;
import console.client.*;

public class StringHandler extends Handler<StringClient, StringEvent, String> {

    @Command(name = "Help", aliases = "help")
    public Object help(ICommandEvent<StringClient, StringEvent, String> event) {
        return super.help(event);
    }
}

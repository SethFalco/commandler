package com.elypia.commandler.string.commandler;

import com.elypia.commandler.*;
import com.elypia.commandler.string.client.*;

public class StringHandler extends Handler<StringClient, StringEvent, String> {

    @Override
    public String help(CommandEvent<StringClient, StringEvent, String> event) {
        return "helped";
    }
}

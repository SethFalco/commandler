package com.elypia.commandlerbot;

import com.elypia.commandler.confiler.DefaultConfiler;
import com.elypia.commandler.events.MessageEvent;

public class CommandlerConfiler extends DefaultConfiler {

    private static final String HELP = "https://alexis.elypia.com/";

    @Override
    public String getHelpUrl(MessageEvent event) {
        return HELP;
    }
}

package com.elypia.commandler;

import com.elypia.commandler.confiler.reactions.DefaultReactionController;
import com.elypia.commandler.events.CommandEvent;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.GenericMessageEvent;

import java.util.*;
import java.util.regex.*;

/**
 * If you're configuing Commandler, it's a good idea to extend and {@link Override}
 * the {@link #getPrefix(GenericMessageEvent)} method here to get you going.
 */

public class JDAConfiler extends Confiler<GenericMessageEvent> {

    private DefaultReactionController REACTION_CONTROLLER;

    private String commandRegex;

    public JDAConfiler(String[] prefixes) {
        this(prefixes, null);
    }

    public JDAConfiler(String[] prefixes, String help) {
        Message
        super(prefixes, help);
    }

    public IReactionController getReactionController() {
        return REACTION_CONTROLLER;
    }
}

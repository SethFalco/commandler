package com.elypia.commandler;

import com.elypia.commandler.impl.*;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.GenericMessageEvent;

public class JDACommandler extends Commandler<JDA, GenericMessageEvent, Message> {

    public JDACommandler(IConfiler<GenericMessageEvent, Message> confiler) {
        super(confiler, new JDADispatcher());
    }
}

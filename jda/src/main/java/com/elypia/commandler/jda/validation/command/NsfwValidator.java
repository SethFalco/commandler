package com.elypia.commandler.jda.validation.command;

import com.elypia.commandler.jda.*;
import com.elypia.commandler.jda.annotations.validation.command.Nsfw;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.events.message.GenericMessageEvent;

public class NsfwValidator implements IJDACommandValidator<Nsfw> {

    @Override
    public boolean validate(JDACommand event, Nsfw annotation) {
        GenericMessageEvent e = event.getSource();
        return e.isFromType(ChannelType.TEXT) && !e.getTextChannel().isNSFW();
    }

    @Override
    public String help(Nsfw annotation) {
        return "This commands can only be done in PMs or NSFW channels.";
    }
}



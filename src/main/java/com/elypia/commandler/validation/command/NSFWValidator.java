package com.elypia.commandler.validation.command;

import com.elypia.commandler.annotations.validation.command.NSFW;
import com.elypia.commandler.events.MessageEvent;
import com.elypia.commandler.validation.ICommandValidator;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.events.message.*;

public class NSFWValidator implements ICommandValidator<NSFW> {

    @Override
    public void validate(MessageEvent event, NSFW annotation) throws IllegalAccessException {
        GenericMessageEvent e = event.getMessageEvent();

        if (!e.isFromType(ChannelType.TEXT))
            return;

        if (!e.getTextChannel().isNSFW())
            throw new IllegalAccessException("This commands can only be performed in NSFW channels.");
    }

    @Override
    public String help(NSFW annotation) {
        return "This commands can only be done in PMs or NSFW channels.";
    }
}

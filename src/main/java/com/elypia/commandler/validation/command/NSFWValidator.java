package com.elypia.commandler.validation.command;

import com.elypia.commandler.annotations.validation.command.NSFW;
import com.elypia.commandler.events.CommandEvent;
import com.elypia.commandler.validation.ICommandValidator;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.events.message.GenericMessageEvent;

public class NSFWValidator implements ICommandValidator<NSFW> {

    @Override
    public boolean validate(CommandEvent event, NSFW annotation) {
        GenericMessageEvent e = event.getMessageEvent();

        if (e.isFromType(ChannelType.TEXT) && !e.getTextChannel().isNSFW())
            return event.invalidate(help(annotation));

        return true;
    }

    @Override
    public String help(NSFW annotation) {
        return "This commands can only be done in PMs or NSFW channels.";
    }
}

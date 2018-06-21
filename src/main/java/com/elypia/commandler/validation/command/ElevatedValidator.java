package com.elypia.commandler.validation.command;

import com.elypia.commandler.annotations.validation.command.Elevated;
import com.elypia.commandler.events.MessageEvent;
import com.elypia.commandler.validation.ICommandValidator;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.GenericMessageEvent;

public class ElevatedValidator implements ICommandValidator<Elevated> {

    @Override
    public boolean validate(MessageEvent event, Elevated annotation) {
        GenericMessageEvent e = event.getMessageEvent();
        TextChannel channel = e.getTextChannel();

        if (e.isFromType(ChannelType.TEXT)) {
            Member member = event.getMessage().getMember();

            if (!member.hasPermission(channel, Permission.MANAGE_SERVER))
                return event.invalidate(help(annotation));
        }

        return true;
    }

    @Override
    public String help(Elevated annotation) {
        return "This commands requires the user have the Manage Server permission to perform this commands.";
    }
}

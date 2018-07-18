package com.elypia.commandler.validation.command;

import com.elypia.commandler.annotations.validation.command.Elevated;
import com.elypia.commandler.CommandEvent;
import com.elypia.commandler.impl.ICommandValidator;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.GenericMessageEvent;

public class ElevatedValidator implements ICommandValidator<Elevated> {

    @Override
    public boolean validate(CommandEvent event, Elevated annotation) {
        User user = event.getMessage().getAuthor();

        for (long id : event.getConfiler().getDevelopers()) {
            if (id == user.getIdLong())
                return true;
        }

        GenericMessageEvent e = event.getMessageEvent();

        if (e.isFromType(ChannelType.TEXT)) {
            TextChannel channel = e.getTextChannel();
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

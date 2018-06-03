package com.elypia.commandler.validation.command;

import com.elypia.commandler.annotations.validation.command.Elevated;
import com.elypia.commandler.events.MessageEvent;
import com.elypia.commandler.validation.ICommandValidator;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class ElevatedValidator implements ICommandValidator<Elevated> {

    @Override
    public void validate(MessageEvent event, Elevated annotation) throws IllegalAccessException {
        MessageReceivedEvent e = event.getMessageEvent();
        TextChannel channel = e.getTextChannel();
        Member member = e.getMember();

        if (!member.hasPermission(channel, Permission.MANAGE_SERVER))
            throw new IllegalAccessException("You must have the `Manage Server` permission to perform this command.");
    }
}

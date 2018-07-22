package com.elypia.commandler.validation.command;

import com.elypia.commandler.*;
import com.elypia.commandler.annotations.validation.command.Secret;
import com.elypia.commandler.impl.ICommandValidator;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.GenericMessageEvent;

import java.util.concurrent.TimeUnit;

public class SecretValidator implements IJDACommandValidator<Secret> {

    private static final String WARNING = "**```diff\n- Delete that message right now and head to DMs. -\n```**";

    @Override
    public boolean validate(JDACommand event, Secret annotation) {
        GenericMessageEvent source = event.getSource();

        if (!source.isFromType(ChannelType.PRIVATE)) {
            if (!event.deleteMessage()) {
                source.getChannel().sendMessage(WARNING).queue(message -> {
                    message.delete().queueAfter(3, TimeUnit.SECONDS);
                });
            }

            event.getMessage().getAuthor().openPrivateChannel().queue(channel -> {
                channel.sendMessage(help(annotation)).queue();
            });

            return false;
        }

        return true;
    }

    @Override
    public String help(Secret annotation) {
        return "This command requires sensitive input which should not be performed infront of other users; only perform this command in PMs.";
    }
}

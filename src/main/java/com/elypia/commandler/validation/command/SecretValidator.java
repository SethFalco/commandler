package com.elypia.commandler.validation.command;

import com.elypia.commandler.annotations.validation.command.Secret;
import com.elypia.commandler.events.CommandEvent;
import com.elypia.commandler.validation.ICommandValidator;
import net.dv8tion.jda.core.entities.ChannelType;

import java.util.concurrent.TimeUnit;

public class SecretValidator implements ICommandValidator<Secret> {

    @Override
    public boolean validate(CommandEvent event, Secret annotation) {
        if (!event.getMessageEvent().isFromType(ChannelType.PRIVATE)) {
            if (!event.tryDeleteMessage()) {
                event.getMessageEvent().getChannel().sendMessage("**```diff\n- Delete that message right now and head to DMs. -\n```**").queue(message -> {
                    message.delete().queueAfter(3, TimeUnit.SECONDS);
                });
            }

            event.getMessage().getAuthor().openPrivateChannel().queue(channel -> {
                channel.sendMessage(help(annotation)).queue();
            });

            return event.invalidate(null);
        }

        return true;
    }

    @Override
    public String help(Secret annotation) {
        return "This command requires sensitive input which should not be performed infront of other users; only perform this command in PMs.";
    }
}

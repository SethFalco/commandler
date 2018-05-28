package com.elypia.commandler.sending.senders;

import com.elypia.commandler.events.MessageEvent;
import com.elypia.commandler.sending.MessageSender;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.requests.restaction.MessageAction;

import java.awt.*;

public class EmbedBuilderSender implements MessageSender<EmbedBuilder> {

    @Override
    public MessageAction send(MessageEvent event, EmbedBuilder toSend) {
        Guild guild = event.getMessageEvent().getGuild();

        if (guild != null) {
            Color color = guild.getSelfMember().getColor();
            toSend.setColor(color);
        }

        return event.getMessageEvent().getChannel().sendMessage(toSend.build());
    }
}

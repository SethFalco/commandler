package com.elypia.commandler;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.GenericMessageEvent;

/**
 * If you're configuing Commandler, it's a good idea to extend and {@link Override}
 * the {@link #getPrefix(GenericMessageEvent)} method here to get you going.
 */
public class JDAConfiler extends Confiler<JDA, GenericMessageEvent, Message> {

    /**
     * Break the command down into it's individual components.
     *
     * @param event The event spanwed by the client.
     * @param content The content of the meessage.
     * @return The input the user provided or null if it's not a valid command.
     */
    @Override
    public JDACommand processEvent(Commandler<JDA, GenericMessageEvent, Message> commandler, GenericMessageEvent event, String content) {
        return new JDACommand(super.processEvent(commandler, event, content));
    }
}

package com.elypia.commandler.jda;

import com.elypia.commandler.*;
import com.elypia.commandler.impl.IConfiler;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.GenericMessageEvent;

/**
 * If you're configuing Commandler, it's a good idea to extend and {@link Override}
 * the {@link IConfiler#getPrefixes(Object)} method here to get you going.
 */
public class JDAConfiler extends Confiler<JDA, GenericMessageEvent, Message> {

    public JDAConfiler(String... prefixes) {
        super(prefixes);
    }

    /**
     * Break the command down into it's individual components.
     *
     * @param event The event spanwed by the client.
     * @param content The content of the meessage.
     * @return The input the user provided or null if it's not a valid command.
     */
    @Override
    public JDACommand processEvent(Commandler<JDA, GenericMessageEvent, Message> commandler, GenericMessageEvent event, String content) {
        var parent = super.processEvent(commandler, event, content);
        return parent != null ? new JDACommand(parent) : null;
    }
}

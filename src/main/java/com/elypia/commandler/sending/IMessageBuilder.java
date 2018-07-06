package com.elypia.commandler.sending;

import com.elypia.commandler.events.AbstractEvent;
import net.dv8tion.jda.core.entities.*;

public interface IMessageBuilder<T> {

    MessageEmbed buildAsEmbed(AbstractEvent event, T toSend);
    MessageEmbed buildAsEmbed(AbstractEvent event, T... toSend);

    String buildAsString(AbstractEvent event, T toSend);
    String buildAsString(AbstractEvent event, T... toSend);
}

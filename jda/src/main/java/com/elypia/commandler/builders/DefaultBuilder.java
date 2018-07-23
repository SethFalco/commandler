package com.elypia.commandler.builders;

import com.elypia.commandler.*;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;

public class DefaultBuilder implements IJDABuilder<String> {

    @Override
    public Message buildEmbed(JDACommand event, String output) {
        return null;
    }

    @Override
    public Message build(JDACommand event, String output) {
        return new MessageBuilder(output).build();
    }
}

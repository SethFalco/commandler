package com.elypia.commandler.example.discord;

import com.elypia.commandler.interfaces.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;

public class DiscordController implements Controller {

    public DiscordController(Dispatcher dispatcher, JDA jda) {
        jda.addEventListener(new DiscordListener(dispatcher, this));
    }

    @Override
    public Class<?> getMessageType() {
        return Message.class;
    }
}

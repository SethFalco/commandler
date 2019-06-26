package com.elypia.commandler.example.discord;

import com.elypia.commandler.CommandlerEvent;
import com.elypia.commandler.annotations.Provider;
import com.elypia.commandler.interfaces.ResponseProvider;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;

import java.net.URL;

@Provider(provides = Message.class, value = {String.class, Character.class, char.class, Boolean.class, boolean.class, URL.class})
public class MiscToMessageProvider implements ResponseProvider<Object, Message> {

    @Override
    public Message provide(CommandlerEvent<?> event, Object output) {
        return new MessageBuilder(output.toString()).build();
    }
}

package com.elypia.commandler.validation.command;

import com.elypia.commandler.*;
import com.elypia.commandler.annotations.validation.command.Scope;
import com.elypia.commandler.impl.ICommandValidator;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.GenericMessageEvent;

import java.util.Arrays;

public class ScopeValidator implements IJDACommandValidator<Scope> {

    @Override
    public boolean validate(JDACommand event, Scope scope) {
        ChannelType[] types = scope.value();

        if (Arrays.asList(types).contains(event.getSource().getChannelType()))
            return true;

        String list = buildList(types);
        String message = "This commands can only be performed in " + list + " channels.";
        return false;
    }

    @Override
    public String help(Scope annotation) {
        return "This commands can only be performed in the following channels: " + buildList(annotation.value()) + ".";
    }

    private String buildList(ChannelType... types) {
        if (types.length == 1)
            return "`" + getFriendlyName(types[0]) + "`";

        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < types.length; i++) {
            ChannelType type = types[i];

            if (i != types.length - 1)
                builder.append("`" + getFriendlyName(type) + "`, ");
            else
                builder.append("and `" + getFriendlyName(type) + "`");
        }

        return builder.toString();
    }

    private String getFriendlyName(ChannelType type) {
        switch (type) {
            case TEXT: return "Guild Text Channels";
            case PRIVATE: return "Private Messages";
            case GROUP: return "Group Chat";
            case VOICE: return "Voice Channels";
            default: return type.name();
        }
    }
}

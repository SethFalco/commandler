package com.elypia.commandler.validation.command;

import com.elypia.commandler.annotations.validation.command.Scope;
import com.elypia.commandler.events.MessageEvent;
import com.elypia.commandler.validation.ICommandValidator;
import net.dv8tion.jda.core.entities.ChannelType;

import java.util.Arrays;

public class ScopeValidator implements ICommandValidator<Scope> {

    @Override
    public void validate(MessageEvent event, Scope scope) throws IllegalAccessException {
        ChannelType[] types = scope.value();

        if (Arrays.asList(types).contains(event.getMessageEvent().getChannelType()))
            return;

        String list = buildList(types);
        String message = "This commands can only be performed in " + list + " channels.";
        throw new IllegalAccessException(message);
    }

    @Override
    public String help(Scope annotation) {
        return "This commands can only be performed in the following channels: " + buildList(annotation.value()) + ".";
    }

    private String buildList(ChannelType... types) {
        if (types.length == 1)
            return "`" + types[0].name() + "`";

        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < types.length; i++) {
            ChannelType type = types[i];

            if (i != types.length - 1)
                builder.append("`" + type.name() + "`, ");
            else
                builder.append("and `" + type.name() + "`");
        }

        return builder.toString();
    }
}

package com.elypia.commandler.validation.command;

import com.elypia.commandler.annotations.validation.command.Scope;
import com.elypia.commandler.events.MessageEvent;
import com.elypia.commandler.validation.ICommandValidator;
import net.dv8tion.jda.core.entities.ChannelType;

import java.util.*;

public class ScopeValidator implements ICommandValidator<Scope> {

    @Override
    public void validate(MessageEvent event, Scope scope) throws IllegalAccessException {
        List<ChannelType> types = Arrays.asList(scope.value());

        if (types.contains(event.getMessageEvent().getChannelType()))
            return;

        String list = buildList(types);
        String message = "This commands can only be performed in " + list + " channels.";
        throw new IllegalAccessException(message);
    }

    private String buildList(List<ChannelType> types) {
        if (types.size() == 1)
            return "`" + types.get(0).name() + "`";

        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < types.size(); i++) {
            ChannelType type = types.get(i);

            if (i != types.size() - 1)
                builder.append("`" + type.name() + "`, ");
            else
                builder.append("and `" + type.name() + "`");
        }

        return builder.toString();
    }
}

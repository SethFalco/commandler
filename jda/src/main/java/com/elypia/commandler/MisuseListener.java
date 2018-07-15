package com.elypia.commandler;

import com.elypia.commandler.annotations.Param;
import com.elypia.commandler.events.CommandEvent;
import com.elypia.commandler.impl.*;
import com.elypia.commandler.metadata.*;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;

import java.util.StringJoiner;

public class MisuseListener implements IMisuseListener<Message> {

    private static final String PARSE_FAILURE = "I couldn't interpret your input, `%s`, as a `%s`!";

    @Override
    public Message parameterCountMismatch(CommandInput input, MetaCommand metaCommand) {
        return null;
    }

    @Override
    public Message noDefaultCommand() {
        return null;
    }

    @Override
    public Message parameterParseFailure(CommandEvent event, Class<?> type, String input) {
        String content = String.format(PARSE_FAILURE, input, type.getSimpleName());
        return new MessageBuilder(content).build();
    }

    @Override
    public Message moduleDisabled(CommandEvent event) {
        return null;
    }

    @Override
    public Message exceptionFailure(ReflectiveOperationException ex) {
        return null;
    }

}

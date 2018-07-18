package com.elypia.commandler.validation.param;

import com.elypia.commandler.annotations.validation.param.Talkable;
import com.elypia.commandler.CommandEvent;
import com.elypia.commandler.metadata.MetaParam;
import com.elypia.commandler.impl.IParamValidator;
import net.dv8tion.jda.core.entities.*;

public class TalkableValidator implements IParamValidator<MessageChannel, Talkable> {

    @Override
    public boolean validate(CommandEvent event, MessageChannel messageChannel, Talkable talkable, MetaParam param) {
        if (messageChannel.getType().isGuild() && !((TextChannel)messageChannel).canTalk())
            return false;

        return true;
    }

    @Override
    public String help(Talkable annotation) {
        return null;
    }
}

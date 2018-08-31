package com.elypia.commandler.jda.validation.param;

import com.elypia.commandler.jda.*;
import com.elypia.commandler.jda.annotations.validation.param.Talkable;
import com.elypia.commandler.metadata.MetaParam;
import net.dv8tion.jda.core.entities.*;

public class TalkableValidator implements IJDAParamValidator<MessageChannel, Talkable> {

    @Override
    public boolean validate(JDACommand event, MessageChannel messageChannel, Talkable talkable, MetaParam param) {
        if (messageChannel.getType().isGuild() && !((TextChannel)messageChannel).canTalk())
            return false;

        return true;
    }

    @Override
    public String help(Talkable annotation) {
        return null;
    }
}

package com.elypia.commandler.jda.validation.param;

import com.elypia.commandler.jda.*;
import com.elypia.commandler.jda.annotations.validation.param.Everyone;
import com.elypia.commandler.metadata.MetaParam;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.events.message.GenericMessageEvent;

public class EveryoneValidator implements IJDAParamValidator<String, Everyone> {

    @Override
    public boolean validate(JDACommand event, String input, Everyone everyone, MetaParam param) {
        GenericMessageEvent source = event.getSource();

        if (source.isFromType(ChannelType.TEXT)) {
            if (!event.getMessage().getMember().hasPermission(source.getTextChannel(), Permission.MESSAGE_MENTION_EVERYONE)) {
                input = input.toLowerCase();

                if (input.contains("@everyone") || input.contains("@here"))
                    return false;
            }
        }

        return true;
    }

    @Override
    public String help(Everyone annotation) {
        return "You must have the Mention Everyone permission in order to mention everyone or here in this parameter.";
    }
}

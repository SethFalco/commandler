package com.elypia.commandler.validation.param;

import com.elypia.commandler.annotations.validation.param.Everyone;
import com.elypia.commandler.events.MessageEvent;
import com.elypia.commandler.metadata.MetaParam;
import com.elypia.commandler.validation.IParamValidator;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.events.message.GenericMessageEvent;

public class EveryoneValidator implements IParamValidator<String, Everyone> {

    @Override
    public boolean validate(MessageEvent event, String input, Everyone everyone,  MetaParam param) {
        GenericMessageEvent e = event.getMessageEvent();

        if (e.isFromType(ChannelType.TEXT)) {
            if (!event.getMessage().getMember().hasPermission(e.getTextChannel(), Permission.MESSAGE_MENTION_EVERYONE)) {
                input = input.toLowerCase();

                if (input.contains("@everyone") || input.contains("@here"))
                    return event.invalidate("You must have the Mention Everyone permission to mention everyone or here for the parameter, `" + param.getParamAnnotation().name() + "`.");
            }
        }

        return true;
    }

    @Override
    public String help(Everyone annotation) {
        return "You must have the Mention Everyone permission in order to mention everyone or here in this parameter.";
    }
}

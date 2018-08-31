package com.elypia.commandler.jda.validation.param;

import com.elypia.commandler.jda.*;
import com.elypia.commandler.jda.annotations.validation.param.Everyone;
import com.elypia.commandler.metadata.MetaParam;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.events.message.GenericMessageEvent;

import java.util.function.Function;

public class EveryoneValidator extends IJDAParamValidator<String, Everyone> {

    private static final Function<Everyone, String> DEFAULT_HELP = (everyone) -> {
        return "You must have the Mention Everyone permission in order to mention everyone or here in this parameter.";
    };

    public EveryoneValidator(Function<Everyone, String> help) {
        super(help);

        if (this.help == null)
            this.help = DEFAULT_HELP;
    }

    public EveryoneValidator() {
        this(DEFAULT_HELP);
    }

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
}

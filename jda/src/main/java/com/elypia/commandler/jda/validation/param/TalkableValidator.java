package com.elypia.commandler.jda.validation.param;

import com.elypia.commandler.jda.*;
import com.elypia.commandler.jda.annotations.validation.param.Talkable;
import com.elypia.commandler.metadata.MetaParam;
import net.dv8tion.jda.core.entities.*;

import java.util.function.Function;

public class TalkableValidator extends IJDAParamValidator<MessageChannel, Talkable> {

    private static final Function<Talkable, String> DEFAULT_HELP = (everyone) -> {
        return "That's not a good idea, you should probably use a channel I can actually send messages in!";
    };

    public TalkableValidator(Function<Talkable, String> help) {
        super(help);

        if (this.help == null)
            this.help = DEFAULT_HELP;
    }

    public TalkableValidator() {
        this(DEFAULT_HELP);
    }

    @Override
    public boolean validate(JDACommand event, MessageChannel messageChannel, Talkable talkable, MetaParam param) {
        return !messageChannel.getType().isGuild() && !((TextChannel)messageChannel).canTalk();
    }
}

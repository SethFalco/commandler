package com.elypia.commandler;

import com.elypia.commandler.annotations.validation.command.*;
import com.elypia.commandler.annotations.validation.param.*;
import com.elypia.commandler.builders.*;
import com.elypia.commandler.impl.*;
import com.elypia.commandler.parsers.*;
import com.elypia.commandler.validation.command.*;
import com.elypia.commandler.validation.param.*;
import net.dv8tion.jda.core.*;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.GenericMessageEvent;
import org.slf4j.*;

public class JDACommandler extends Commandler<JDA, GenericMessageEvent, Message> {

    private static final Logger logger = LoggerFactory.getLogger(JDACommandler.class);

    public JDACommandler(JDA client, String... prefixes) {
        // ? Initialise Commandler
        super(new JDAConfiler(prefixes));

        this.client = client;
        dispatcher = new JDADispatcher(this);

        // ? Register any builders we have with the builder
        registerBuilder(new EmbedBuilderBuilder(), String.class);
        registerBuilder(new MessageEmbedBuilder(), NumberParser.TYPES);

        registerValidator(Nsfw.class, new NsfwValidator());
        registerValidator(Permissions.class, new PermissionValidator());
        registerValidator(Scope.class, new ScopeValidator());
        registerValidator(Secret.class, new SecretValidator());

        registerValidator(Everyone.class, new EveryoneValidator());
        registerValidator(Talkable.class, new TalkableValidator());

        registerParser(new EmoteParser(), Emote.class);
        registerParser(new GuildParser(), Guild.class);
        registerParser(new RoleParser(), Role.class);
        registerParser(new TextChannelParser(), TextChannel.class);
        registerParser(new UserParser(), User.class);
        registerParser(new VoiceChannelParser(), VoiceChannel.class);

        logger.info("New instance of StringCommandler succesfully initialised.");
    }

    @Override
    public Message trigger(GenericMessageEvent event, String input) {
        return dispatcher.processEvent(event, input);
    }
}

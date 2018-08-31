package com.elypia.commandler.jda;

import com.elypia.commandler.Commandler;
import com.elypia.commandler.impl.ICommandEvent;
import com.elypia.commandler.jda.annotations.validation.command.*;
import com.elypia.commandler.jda.annotations.validation.param.*;
import com.elypia.commandler.jda.builders.*;
import com.elypia.commandler.jda.parsers.*;
import com.elypia.commandler.jda.validation.command.*;
import com.elypia.commandler.jda.validation.param.*;
import com.elypia.commandler.registers.BuildRegister;
import net.dv8tion.jda.core.*;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.GenericMessageEvent;
import org.slf4j.*;

import java.util.Objects;

public class JDACommandler extends Commandler<JDA, GenericMessageEvent, Message> {

    private static final Logger logger = LoggerFactory.getLogger(JDACommandler.class);

    public JDACommandler(JDA client, String... prefixes) {
        this(client, new JDAConfiler(prefixes));
    }

    public JDACommandler(JDA client, JDAConfiler confiler) {
        super(confiler);

        this.client = client;
        dispatcher = new JDADispatcher(this);
        builder = new BuildRegister<>() {

            @Override
            public Message build(ICommandEvent<?, ?, Message> event, Object object) {
                Objects.requireNonNull(object);
                IJDABuilder builder = (IJDABuilder)getBuilder(object.getClass());

                GenericMessageEvent source = (GenericMessageEvent)event.getSource();
                Message embed = builder.buildEmbed((JDACommand)event, object);

                if (embed != null) {
                    if (!source.getChannelType().isGuild())
                        return new MessageBuilder(embed).build();

                    Member self = source.getGuild().getSelfMember();

                    if (self.hasPermission(source.getTextChannel(), Permission.MESSAGE_EMBED_LINKS))
                        return new MessageBuilder(embed).build();
                }

                Message content = builder.build((JDACommand)event, object);

                if (content == null) {
                    String builderName = builder.getClass().getName();
                    String objectName = object.getClass().getName();
                    throw new IllegalStateException(String.format(BuildRegister.NULL_DEFAULT_BUILDER, builderName, objectName));
                }

                return content;
            }
        };

        // ? Register any builders we have with the builder
        registerBuilder(new EmbedBuilderBuilder(), EmbedBuilder.class);
        registerBuilder(new MessageEmbedBuilder(), MessageEmbedBuilder.class);
        registerBuilder(new DefaultBuilder(), String.class);

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
    public Message trigger(GenericMessageEvent event, String input, boolean send) {
        return dispatcher.processEvent(event, confiler.getPrefixes(event)[0] + input, send);
    }
}

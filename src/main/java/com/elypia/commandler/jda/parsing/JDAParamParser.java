package com.elypia.commandler.jda.parsing;

import com.elypia.commandler.jda.parsing.parsers.*;
import com.elypia.commandler.parsing.ParamParser;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.*;

public class JDAParamParser extends ParamParser {

    private JDA jda;

    public JDAParamParser(JDA jda) {
        super();

        registerParser(Emote.class, new EmoteParser(jda));
        registerParser(Guild.class, new GuildParser(jda));
        registerParser(Role.class, new RoleParser(jda));
        registerParser(TextChannel.class, new TextChannelParser(jda));
        registerParser(VoiceChannel.class, new VoiceChannelParser(jda));
    }
}

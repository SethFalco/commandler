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

    @Override
    public Object parseParam(Object object, Class<?> clazz) throws IllegalArgumentException {
        boolean array = object.getClass().isArray();

        if (clazz.isArray()) {
            String[] input = array ? (String[])object : new String[] {(String)object};
            Object[] objects = new Object[input.length];

            for (int i = 0; i < input.length; i++)
                objects[i] = parsers.get(clazz.getComponentType()).parse(input[i]);

            return objects;
        } else {
            if (array)
                throw new IllegalArgumentException("Parameter `" + String.join(", ", object + "` can't be a list."));

            String input = (String)object;

            return parsers.get(clazz).parse(input);
        }
    }
}

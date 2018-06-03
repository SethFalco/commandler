package com.elypia.commandler.parsing;

import com.elypia.commandler.annotations.filter.Search;
import com.elypia.commandler.data.SearchScope;
import com.elypia.commandler.events.MessageEvent;
import com.elypia.commandler.metadata.MetaParam;
import com.elypia.commandler.parsing.parsers.*;
import com.elypia.commandler.parsing.parsers.jda.*;
import net.dv8tion.jda.core.entities.*;

import java.lang.reflect.Array;
import java.net.URL;
import java.util.*;

public class Parser {

    protected Map<Class<?>, IParamParser> parsers;

    public Parser() {
        this(true);
    }

    public Parser(boolean auto) {
        parsers = new HashMap<>();

        if (auto) {
            registerParser(boolean.class, new BooleanParser());
            registerParser(double.class, new DoubleParser());
            registerParser(int.class, new IntParser());
            registerParser(long.class, new LongParser());
            registerParser(String.class, new StringParser());
            registerParser(URL.class, new UrlParser());

            registerParser(Emote.class, new EmoteParser());
            registerParser(Guild.class, new GuildParser());
            registerParser(Role.class, new RoleParser());
            registerParser(TextChannel.class, new TextChannelParser());
            registerParser(User.class, new UserParser());
            registerParser(VoiceChannel.class, new VoiceChannelParser());
        }
    }

    public <T> void registerParser(Class<T> t, IParamParser<T> parser) {
        if (parsers.keySet().contains(t))
            throw new IllegalArgumentException("Parser for this type of object has already been registered.");

        parsers.put(t, parser);
    }

    public Object parseParam(MessageEvent event, MetaParam param, Object object) throws IllegalArgumentException {
        Class<?> clazz = param.getParameter().getType(); // Class of type required
        boolean array = clazz.isArray(); // Is type required an array
        SearchScope scope = null; // The search scope if applicable

        Search search = param.getParameter().getAnnotation(Search.class);

        scope = search != null ? search.value() : SearchScope.GLOBAL;

        if (clazz.isArray()) {
            String[] input = object.getClass().isArray() ? (String[])object : new String[] {(String)object};
            Object[] objects = (Object[])Array.newInstance(clazz.getComponentType(), input.length);

            for (int i = 0; i < input.length; i++)
                objects[i] = parsers.get(clazz.getComponentType()).parse(event, scope, input[i]);

            return objects;
        } else {
            if (array)
                throw new IllegalArgumentException("Parameter `" + String.join(", ", object + "` can't be a list."));

            String input = (String)object;

            return parsers.get(clazz).parse(event, scope, input);
        }
    }
}

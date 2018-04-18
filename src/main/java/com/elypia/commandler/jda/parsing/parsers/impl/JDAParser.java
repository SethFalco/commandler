package com.elypia.commandler.jda.parsing.parsers.impl;

import com.elypia.commandler.jda.data.SearchScope;
import com.elypia.commandler.events.MessageEvent;
import com.elypia.commandler.parsing.impl.IParser;
import net.dv8tion.jda.core.JDA;

public abstract class JDAParser<T> implements IParser<T> {

    protected final JDA jda;

    public JDAParser(JDA jda) {
        this.jda = jda;
    }

    public abstract T parse(MessageEvent event, String input, SearchScope scope);

    @Override
    public T parse(String input) {
        return parse(null, input, SearchScope.GLOBAL);
    }
}

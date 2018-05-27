package com.elypia.commandler.parsing.impl;

import com.elypia.commandler.data.SearchScope;
import com.elypia.commandler.events.MessageEvent;
import net.dv8tion.jda.core.JDA;

public abstract class JDAParser<T> {

    protected JDA jda;

    public JDAParser(JDA jda) {
        this.jda = jda;
    }

    public JDAParser() {

    }

    public abstract T parse(MessageEvent event, SearchScope scope, String input);

    public JDA getJDA() {
        return jda;
    }
}

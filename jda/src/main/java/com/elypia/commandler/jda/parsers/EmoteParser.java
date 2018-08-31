package com.elypia.commandler.jda.parsers;

import com.elypia.commandler.jda.*;
import net.dv8tion.jda.core.entities.Emote;

import java.util.*;

public class EmoteParser implements IJDAParser<Emote> {

    @Override
    public Emote parse(JDACommand event, Class<? extends Emote> type, String input) {
        Set<Emote> emotes = new HashSet<>(event.getClient().getEmotes());
        emotes.addAll(event.getMessage().getEmotes());

        List<Emote> matches = new ArrayList<>();

        emotes.forEach(emote -> {
            if (emote.getId().equals(input))
                matches.add(emote);

            else if (emote.getAsMention().equalsIgnoreCase(input))
                matches.add(emote);

            else if (emote.getName().equalsIgnoreCase(input))
                matches.add(emote);

            else if (emote.toString().equalsIgnoreCase(input))
                matches.add(emote);
        });

        return matches.isEmpty() ? null : matches.get(0);
    }
}

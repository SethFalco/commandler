package com.elypia.commandlerbot.modules;

import com.elypia.commandler.CommandHandler;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.events.MessageEvent;
import com.elypia.elypiai.urbandictionary.UrbanDefinition;
import com.elypia.elypiai.urbandictionary.UrbanDictionary;
import com.elypia.elypiai.urbandictionary.data.UrbanResultType;
import net.dv8tion.jda.core.EmbedBuilder;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Module(
    name = "UrbanDictionary",
    aliases = {"urbandictionary", "urbandict", "urban", "ud"},
    description = "An online dictionary defined by the community for definitions and examples."
)
public class UrbanDictionaryModule extends CommandHandler {

    private UrbanDictionary dict;

    public UrbanDictionaryModule() {
        dict = new UrbanDictionary();
    }

    @Static
    @CommandGroup("define")
    @Command(aliases = "define", help = "Return the definition of a word or phrase.")
    @Param(name = "body", help = "Word or phrase to define!")
    public void define(MessageEvent event, String body[]) throws InterruptedException {
        if (body.length == 1) {
            define(event, body[0], true);
            return;
        }

        CountDownLatch latch = new CountDownLatch(body.length);
        EmbedBuilder builder = new EmbedBuilder();

        for (String s : body) {
            dict.define(s, results -> {
                if (results.getResultType() == UrbanResultType.NO_RESULTS)
                    return;

                UrbanDefinition definition = results.getResult(true);

                String name = String.format("👍: %,d 👎: %,d\n", definition.getThumbsUp(), definition.getThumbsDown());
                builder.addField(definition.getWord() + " " + name, definition.getDefinition(), true);

                latch.countDown();
            }, failure -> {
                failure.printStackTrace();
                latch.countDown();
            });
        }

        latch.await(10, TimeUnit.SECONDS);
        event.reply(builder);
    }

    @CommandGroup("define")
    @Param(name = "body", help = "Word or phrase to define!")
    @Param(name = "random", help = "Random result or top result!")
    @Reaction(alias = "🔉", help = "Hear an audio clip associtated with this word.")
    @Reaction(alias = "🎲", help = "Don't like definition? Get a new one!")
    public void define(MessageEvent event, String body, boolean random) {
        dict.define(body, results -> {
            if (results.getResultType() == UrbanResultType.NO_RESULTS) {
                event.reply("Sorry I didn't find any results. :c");
                return;
            }

            UrbanDefinition definition = results.getResult(random);

            EmbedBuilder builder = new EmbedBuilder();
            builder.setAuthor(definition.getAuthor());

            String titleText = definition.getWord();
            builder.setTitle(titleText, definition.getPermaLink());

            builder.setDescription(definition.getDefinition());

            String descText = String.format (
                "%s\n\n👍: %,d 👎: %,d",
                definition.getExample(),
                definition.getThumbsUp(),
                definition.getThumbsDown()
            );
            builder.addField("Example", descText, true);

            event.reply(builder);
        }, failure -> failure.printStackTrace());
    }
}

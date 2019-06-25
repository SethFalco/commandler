package com.elypia.commandler.example.discord;

import com.elypia.commandler.interfaces.*;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.*;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.*;

public class DiscordListener extends ListenerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(DiscordListener.class);

    private final Dispatcher dispatcher;
    private final Controller controller;

    public DiscordListener(final Dispatcher dispatcher, Controller controller) {
        this.dispatcher = dispatcher;
        this.controller = controller;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot())
            return;

        String content = event.getMessage().getContentRaw();

        if (!dispatcher.isValid(event, content))
            return;

        Object object = dispatcher.dispatch(controller, event, content);

        if (object != null)
            event.getChannel().sendMessage((Message)object).queue();
    }

    @Override
    public void onMessageUpdate(MessageUpdateEvent event) {
        String content = event.getMessage().getContentRaw();

        if (!dispatcher.isValid(event, content) || event.getAuthor().isBot())
            return;

        Message updatedMessage = event.getMessage();

        event.getChannel().getHistoryAfter(updatedMessage.getIdLong(), 1).queue(history -> {
            if (history.isEmpty())
                event.getChannel().sendMessage((Message)dispatcher.dispatch(controller, event, content)).queue();
            else {
                Message nextMessage = history.getRetrievedHistory().get(0);

                if (nextMessage.getAuthor().getIdLong() == event.getJDA().getSelfUser().getIdLong())
                    nextMessage.editMessage((Message)dispatcher.dispatch(controller, event, content)).queue();
            }
        });
    }
}

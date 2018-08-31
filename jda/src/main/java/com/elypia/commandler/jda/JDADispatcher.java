package com.elypia.commandler.jda;

import com.elypia.commandler.*;
import com.elypia.commandler.impl.IConfiler;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.message.*;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.core.hooks.*;

import java.time.OffsetDateTime;

public class JDADispatcher extends Dispatcher<JDA, GenericMessageEvent, Message> implements EventListener {

    /**
     * The JDADispatcher is what recieved JDA events and where Commandler begins
     * to parse events and determine the module / command combo and execute the method. <br>
     * Note: This will only perform events registered to Commandler and not other
     * {@link ListenerAdapter}s that may be associated with {@link JDA}.
     *
     * @param commandler The parent Commandler instance.
     */
    public JDADispatcher(final JDACommandler commandler) {
        super(commandler);
        commandler.getClient().addEventListener(this);
    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof MessageReactionAddEvent)
            onMessageReactionAdd((MessageReactionAddEvent)event);
        else if (event instanceof MessageReceivedEvent)
            onMessageReceived((MessageReceivedEvent)event);
        else if (event instanceof MessageUpdateEvent)
            onMessageUpdate((MessageUpdateEvent)event);
    }

    // ! I'll readd reaction handling at some point.
    public void onMessageReactionAdd(MessageReactionAddEvent event) {
//        User user = event.getUser();
//
//        if (user == event.getJDA().getSelfUser())
//            return;
//
//        IReactionListener controller = confiler.getReactionListener();
//        ReactionRecord record = controller.getReactionRecord(event.getMessageIdLong());
//
//        if (record == null)
//            return;
//
//        if (user.isBot()) {
//            event.getReaction().removeReaction(user).queue();
//            return;
//        }
//
//        event.getReaction().getUsers().queue(users -> {
//            if (users.contains(user)) {
//                ReactionEvent reactionEvent = new ReactionEvent(commandler, event, record);
//                MetaCommand metaCommand = commandler.getCommands().get(record.getCommandId());
//                Method method = metaCommand.getMetaModule().getReactionEvent(record.getCommandId(), event.getReactionEmote().getName());
//
//                try {
//                    Object object = method.invoke(metaCommand.getHandler(), reactionEvent);
//
//                    if (object != null)
//                        reactionEvent.setMessage(builder.buildMessage(reactionEvent, object));
//
//                    if (event.getChannelType().isGuild()) {
//                        if (event.getGuild().getSelfMember().hasPermission(event.getTextChannel(), Permission.MESSAGE_MANAGE))
//                            event.getReaction().removeReaction(event.getUser()).queue();
//                    }
//                } catch (IllegalAccessException | InvocationTargetException e) {
//                    logger.
//                }
//            }
//        });
    }

    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot())
            return;

        processEvent(event, event.getMessage().getContentRaw(), true);
    }

    public void onMessageUpdate(MessageUpdateEvent event) {
        if (event.getAuthor().isBot())
            return;

        Message message = event.getMessage();
        OffsetDateTime timestamp = message.getCreationTime();
        OffsetDateTime accepted = OffsetDateTime.now().minusMinutes(1);

        if (timestamp.isBefore(accepted))
            return;

        event.getChannel().getHistoryAfter(message.getIdLong(), 1).queue(history -> {
           if (history.isEmpty())
               processEvent(event, event.getMessage().getContentRaw(), true);
        });
    }

    // ! The way we're doing this is pretty gross, look into a better way.
    @Override
    public Message processEvent(GenericMessageEvent source, String content, boolean send){
        return super.processEvent(source, content, send);
    }

    @Override
    public Commandler<JDA, GenericMessageEvent, Message> getCommandler() {
        return commandler;
    }

    @Override
    public IConfiler<JDA, GenericMessageEvent, Message> getConfiler() {
        return confiler;
    }
}

package com.elypia.commandler;

import com.elypia.commandler.impl.*;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.*;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.time.OffsetDateTime;

public class JDADispatcher extends ListenerAdapter implements IDispatcher<JDA, GenericMessageEvent, Message> {

    /**
     * The {@link Commandler} instance which is what gives access to all
     * registers to add modules, parsers, senders, and validators.
     */

    private final JDACommandler commandler;

    private final JDAConfiler confiler;

    /**
     * The JDADispatcher is what recieved JDA events and where Commandler begins
     * to parse events and determine the module / command combo and execute the method. <br>
     * Note: This will only perform events registered to Commandler and not other
     * {@link ListenerAdapter}s that may be associated with {@link JDA}.
     *
     * @param commandler The parent Commandler instance.
     */

    public JDADispatcher(final JDACommandler commandler) {
        this.commandler = commandler;
        confiler = (JDAConfiler)commandler.getConfiler();
    }

//    @Override
//    public void onMessageReactionAdd(MessageReactionAddEvent event) {
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
//    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        processEvent(event, event.getMessage().getContentRaw());
    }

    @Override
    public void onMessageUpdate(MessageUpdateEvent event) {
        Message message = event.getMessage();
        OffsetDateTime timestamp = message.getCreationTime();
        OffsetDateTime accepted = OffsetDateTime.now().minusMinutes(1);

        if (timestamp.isBefore(accepted))
            return;

        event.getChannel().getHistoryAfter(message.getIdLong(), 1).queue(history -> {
           if (history.isEmpty())
               processEvent(event, event.getMessage().getContentRaw());
        });
    }

    @Override
    public Message processEvent(GenericMessageEvent event) {
        long id = event.getMessageIdLong();
        Message message = event.getChannel().getMessageById(id).complete();
        return processEvent(event, message.getContentRaw());
    }

    @Override
    public Commandler getCommandler() {
        return commandler;
    }

    @Override
    public IConfiler getConfiler() {
        return confiler;
    }
}

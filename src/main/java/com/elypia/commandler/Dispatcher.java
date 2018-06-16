package com.elypia.commandler;

import com.elypia.commandler.annotations.Reaction;
import com.elypia.commandler.events.MessageEvent;
import com.elypia.commandler.metadata.*;
import com.elypia.commandler.modules.CommandHandler;
import com.elypia.commandler.parsing.Parser;
import com.elypia.commandler.sending.Sender;
import com.elypia.commandler.validation.Validator;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.*;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.lang.reflect.InvocationTargetException;
import java.time.OffsetDateTime;

public class Dispatcher extends ListenerAdapter {

    private final Commandler commandler;
    private final Parser parser;
    private final Sender sender;
    private final Validator validator;

    public Dispatcher(final Commandler commandler) {
        this.commandler = commandler;
        this.parser = new Parser();
        this.sender = new Sender();
        this.validator = new Validator(commandler);
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        process(event, event.getMessage());
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
                process(event, event.getMessage());
        });
    }

    public void process(GenericMessageEvent messageEvent, Message msg) {
        process(messageEvent, msg, msg.getContentRaw());
    }

    public void process(GenericMessageEvent messageEvent, Message msg, String content) {
        if (msg.getAuthor().isBot())
            return;

        MessageEvent event = new MessageEvent(commandler, messageEvent, msg, content);

        if (!event.isValid())
            return;

        MetaCommand metaCommand = getMetaCommand(event);
        MessageChannel channel = messageEvent.getChannel();

        if (metaCommand == null) {
            channel.sendMessage("The commands you just tried to perform doesn't exist. Perhaps you should perform the help commands?").queue();
            return;
        }

        if (event.getParams().size() != metaCommand.getMetaParams().stream().filter(o -> o.getParameter().getType() != MessageEvent.class).count()) {
            channel.sendMessage("You didn't give the correct number of parameters for that commands, perhaps try help instead?").queue();
            return;
        }

        try {
            Object[] params = parser.parseParameters(event, metaCommand);
            validator.validate(event, metaCommand, params);

            try {
                Object message = metaCommand.getMethod().invoke(metaCommand.getHandler(), params);

                if (message != null) {
                    sender.sendAsMessage(event, message, o -> {
                        Reaction[] reactions = metaCommand.getMethod().getAnnotationsByType(Reaction.class);

                        for (Reaction reaction : reactions)
                            o.addReaction(reaction.alias()).queue();
                    });
                }
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                ex.printStackTrace();
                channel.sendMessage("Sorry! Something went wrong and I was unable to perform that commands.").queue();
            }
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            channel.sendMessage(ex.getMessage()).queue();
        }
    }

    private MetaCommand getMetaCommand(MessageEvent event) {
        for (CommandHandler handler : commandler.getHandlers()) {
            MetaModule metaModule = handler.getModule();
            String module = event.getModule();
            String command = event.getCommand();

            if (metaModule.hasPerformed(module)) {
                if (command != null) {
                    for (MetaCommand metaCommand : metaModule.getMetaCommands()) {
                        if (metaCommand.getAliases().contains(command))
                            return metaCommand;
                    }

                    event.getParams().add(0, command);
                    event.setCommand(metaModule.getDefaultCommand());
                } else {
                    event.setCommand(metaModule.getDefaultCommand());
                }

                return metaModule.getDefaultCommand();
            }

            for (MetaCommand metaCommand : metaModule.getMetaCommands()) {
                if (metaCommand.isStatic()) {
                    if (metaCommand.getAliases().contains(module)) {
                        event.setModule(handler);

                        if (command != null)
                            event.getParams().add(0, command);

                        event.setCommand(metaCommand);
                        return metaCommand;
                    }
                }
            }
        }

        return null;
    }

    public Parser getParser() {
        return parser;
    }

    public Sender getSender() {
        return sender;
    }

    public Validator getValidator() {
        return validator;
    }
}

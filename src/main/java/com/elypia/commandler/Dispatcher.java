package com.elypia.commandler;

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

        MessageChannel channel = event.getMessageEvent().getChannel();

        if (!parseCommand(event)) {
            onInvalidatedEvent(event);
            return;
        }

        MetaCommand metaCommand = event.getMetaCommand();

        Object[] params = parser.parseParameters(event, metaCommand);

        if (!event.isValid()) {
            onInvalidatedEvent(event);
            return;
        }

        if (validator.validate(event, metaCommand, params)) {
            onInvalidatedEvent(event);
            return;
        }

        try {
            Object message = metaCommand.getMethod().invoke(metaCommand.getHandler(), params);

            if (message != null)
                sender.sendAsMessage(event, message);
        } catch (IllegalAccessException | InvocationTargetException ex) {
            channel.sendMessage("Sorry! Something went wrong and I was unable to perform that commands.").queue();
            ex.printStackTrace();
        }
    }

    private void onInvalidatedEvent(MessageEvent event) {
        String error = event.getErrorMessage();

        if (error != null)
            event.getMessageEvent().getChannel().sendMessage(error).queue();
    }

    /**
     * If the command follows valid syntax, actually parse it to find out
     * what module and command was performed.
     *
     * @param event The message event that the user triggered.
     * @return If the command is still valid.
     */

    private boolean parseCommand(MessageEvent event) {
        return getMetaModule(event);
    }

    /**
     * Obtain the module the user intended, if no module can be found
     * fail silently as the user may not have intended to use out bot at all.
     * If a module is found, go to find the command as the next step via
     * {@link #getMetaCommand(MetaModule, MessageEvent)}. <br>
     * These are stored using the {@link MessageEvent} setter methods.
     *
     * @param event The message event that the user triggered.
     * @return If the command is still valid.
     */

    private boolean getMetaModule(MessageEvent event) {
        String module = event.getModule();
        String command = event.getCommand();

        for (CommandHandler handler : commandler.getHandlers()) {
            MetaModule metaModule = handler.getModule();

            if (metaModule.hasPerformed(module)) {
                event.setMetaModule(metaModule);
                return getMetaCommand(metaModule, event);
            }

            for (MetaCommand metaCommand : metaModule.getStaticCommands()) {
                if (metaCommand.hasPerformed(module)) {
                    if (command != null)
                        event.getParams().add(0, command);

                    event.setMetaModule(metaModule);
                    event.setMetaCommand(metaCommand);

                    if (event.getParams().size() != metaCommand.getInputRequired())
                        return event.invalidate("The parameters you provided doesn't match up with the paramaters this command required.");

                    return true;
                }
            }
        }

        return event.invalidate(null);
    }

    /**
     * Obtain the command the user intended.
     *
     * @param event The message event that the user triggered.
     * @return If the command is still valid.
     */

    private boolean getMetaCommand(MetaModule metaModule, MessageEvent event) {
        String command = event.getCommand();

        if (command != null) {
            MetaCommand metaCommand = metaModule.getCommand(command);

            if (metaCommand != null) {
                event.setMetaCommand(metaCommand);

                if (event.getParams().size() != metaCommand.getInputRequired())
                    return event.invalidate("You specified a valid command however the number of parameters you provided didn't match with what I was expecting.");

                return true;
            }
        }

        MetaCommand defaultCommand = metaModule.getDefaultCommand();

        if (defaultCommand == null)
            return event.invalidate("You've specified a module without a valid command, however there is no default command associated with this module.");

        if (command != null)
            event.getParams().add(0, command);

        if (event.getParams().size() != defaultCommand.getInputRequired())
            return event.invalidate("It seems the command you attemped to do doesn't exist, maybe you should try the help command instead?");

        event.setMetaCommand(defaultCommand);
        return true;
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

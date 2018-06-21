package com.elypia.commandler;

import com.elypia.commandler.events.MessageEvent;
import com.elypia.commandler.metadata.*;
import com.elypia.commandler.modules.CommandHandler;
import com.elypia.commandler.parsing.Parser;
import com.elypia.commandler.sending.Sender;
import com.elypia.commandler.validation.Validator;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.*;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.lang.reflect.InvocationTargetException;
import java.time.OffsetDateTime;

public class Dispatcher extends ListenerAdapter {

    /**
     * The {@link Commandler} instance which is what gives access to all
     * registers to add modules, parsers, senders, and validators.
     */

    private final Commandler commandler;

    /**
     * Parser is the registry that allows you to define how objects are
     * intepretted from the raw string provided in chat.
     */

    private final Parser parser;

    /**
     * Sender is the register that allows you to define how objects are
     * sent. This allows Commandler to know how certain return types
     * need to be processed in order to send in chat.
     */

    private final Sender sender;

    /**
     * Validator is a registery that allows you to define custom annotations
     * and mark them above methods or parameters in order to perform validation.
     * This is useful to ensure a unifed approach is done and to reduce code
     * required to make commands.
     */

    private final Validator validator;

    /**
     * The Dispatcher is what recieved JDA events and where Commandler begins
     * to parse events and determine the module / command combo and execute the method. <br>
     * Note: This will only perform events registered to Commandler and not other
     * {@link ListenerAdapter}s that may be associated with {@link JDA}.
     *
     * @param commandler The parent Commandler instance.
     */

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

    /**
     * Begin processing the event when it occurs. This is the same as calling
     * {@link #process(GenericMessageEvent, Message, String)} with the default content
     * as the {@link Message} content.
     *
     * @param messageEvent The generic message event that occured.
     * @param msg The message that triggered the event.
     */

    public void process(GenericMessageEvent messageEvent, Message msg) {
        process(messageEvent, msg, msg.getContentRaw());
    }

    /**
     * Begin processing the event when it occurs. There are multiple steps to
     * processing the event. <br>
     * If it's a bot, ignore the event. <br>
     * If the event is invalid, ignore the event. <br>
     * Parse the module and command, this may invalidate the event. <br>
     * Validate the command, not the parameters, this may invalidate the command. <br>
     * Parse all parameters from Strings to the required types, this may invalidate the event. <br>
     * Validate all parameters according to any annotations, this may invalidate the event. <br>
     * Send the result in chat if it's not null, if the result is null, assume the user used {@link MessageEvent#reply(Object)}}.
     *
     * @param messageEvent The generic message event that occured.
     * @param msg The message that triggered the event.
     */

    public void process(GenericMessageEvent messageEvent, Message msg, String content) {
        if (msg.getAuthor().isBot())
            return;

        MessageEvent event = new MessageEvent(commandler, messageEvent, msg, content);

        if (!event.isValid() || !parseCommand(event))
            return;

        MetaCommand metaCommand = event.getMetaCommand();

        if (!validator.validateCommand(event, metaCommand))
            return;

        Object[] params = parser.parseParameters(event, metaCommand);

        if (params == null || !validator.validateParams(event, metaCommand, params))
            return;

        if (!event.getCommand().equalsIgnoreCase("help") && !event.getMetaModule().getHandler().isEnabled()) {
            event.invalidate("I apologise, this module is disabled due to live issues.");
            return;
        }

        try {
            Object message = metaCommand.getMethod().invoke(metaCommand.getHandler(), params);

            if (message != null)
                sender.sendAsMessage(event, message);
        } catch (IllegalAccessException | InvocationTargetException ex) {
            sender.sendAsMessage(event, "Sorry! Something went wrong and I was unable to perform that commands.");
            ex.printStackTrace();
        }
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

                if (event.getParams().size() != metaCommand.getInputRequired()) {
                    for (MetaCommand metaOverload : metaCommand.getOverloads()) {
                        if (event.getParams().size() == metaOverload.getInputRequired()) {
                            event.setMetaCommand(metaOverload);
                            return true;
                        }
                    }

                    return event.invalidate("You specified a valid command however the number of parameters you provided didn't match with what I was expecting.");
                }

                return true;
            }
        }

        MetaCommand defaultCommand = metaModule.getDefaultCommand();

        if (defaultCommand == null)
            return event.invalidate("You've specified a module without a valid command, however there is no default command associated with this module.");

        if (command != null)
            event.getParams().add(0, command);

        if (event.getParams().size() != defaultCommand.getInputRequired()) {
            for (MetaCommand metaOverload : defaultCommand.getOverloads()) {
                if (event.getParams().size() == metaOverload.getInputRequired()) {
                    event.setMetaCommand(metaOverload);
                    return true;
                }
            }

            return event.invalidate("It seems the command you attemped to do doesn't exist, maybe you should try the help command instead?");
        }

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

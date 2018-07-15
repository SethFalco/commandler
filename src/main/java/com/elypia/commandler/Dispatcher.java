package com.elypia.commandler;

import com.elypia.commandler.building.Builder;
import com.elypia.commandler.confiler.reactions.ReactionRecord;
import com.elypia.commandler.events.*;
import com.elypia.commandler.impl.*;
import com.elypia.commandler.metadata.*;
import com.elypia.commandler.modules.CommandHandler;
import com.elypia.commandler.parsing.Parser;
import com.elypia.commandler.validation.Validator;
import net.dv8tion.jda.core.*;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.*;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.lang.reflect.*;
import java.time.OffsetDateTime;
import java.util.Collections;

public class Dispatcher extends ListenerAdapter {

    /**
     * The {@link Commandler} instance which is what gives access to all
     * registers to add modules, parsers, senders, and validators.
     */

    private final Commandler commandler;

    private final IConfiler confiler;

    /**
     * Parser is the registry that allows you to define how objects are
     * intepretted from the raw string provided in chat.
     */

    private final Parser parser;

    /**
     * Builder is the register that allows you to define how objects are
     * sent. This allows Commandler to know how certain return types
     * need to be processed in order to send in chat.
     */

    private final Builder builder;

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

        confiler = commandler.getConfiler();
        parser = new Parser();
        builder = new Builder(commandler);
        validator = new Validator(commandler);
    }

    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event) {
        User user = event.getUser();

        if (user == event.getJDA().getSelfUser())
            return;

        IReactionController controller = confiler.getReactionController();
        ReactionRecord record = controller.getReactionRecord(event.getMessageIdLong());

        if (record == null)
            return;

        if (user.isBot()) {
            event.getReaction().removeReaction(user).queue();
            return;
        }

        event.getReaction().getUsers().queue(users -> {
            if (users.contains(user)) {
                ReactionEvent reactionEvent = new ReactionEvent(commandler, event, record);
                MetaCommand metaCommand = commandler.getCommands().get(record.getCommandId());
                Method method = metaCommand.getMetaModule().getReactionEvent(record.getCommandId(), event.getReactionEmote().getName());

                try {
                    Object object = method.invoke(metaCommand.getHandler(), reactionEvent);

                    if (object != null)
                        reactionEvent.setMessage(builder.buildMessage(reactionEvent, object));

                    if (event.getChannelType().isGuild()) {
                        if (event.getGuild().getSelfMember().hasPermission(event.getTextChannel(), Permission.MESSAGE_MANAGE))
                            event.getReaction().removeReaction(event.getUser()).queue();
                    }
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        processCommand(event, event.getMessage());
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
               processCommand(event, event.getMessage());
        });
    }

    /**
     * Begin processing the event when it occurs. This is the same as calling
     * {@link #processCommand(GenericMessageEvent, Message, String)} with the default content
     * as the {@link Message} content.
     *
     * @param messageEvent The generic message event that occured.
     * @param msg The message that triggered the event.
     */

    public void processCommand(GenericMessageEvent messageEvent, Message msg) {
        processCommand(messageEvent, msg, msg.getContentRaw());
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
     * Send the result in chat if it's not null, if the result is null, assume the user used {@link CommandEvent#reply(Object)}}.
     *
     * @param messageEvent The generic message event that occured.
     * @param msg The message that triggered the event.
     */

    public void processCommand(GenericMessageEvent messageEvent, Message msg, String content) {
        User author = msg.getAuthor();

        if (author.isBot() && author != msg.getJDA().getSelfUser())
            return;

        CommandInput input = confiler.processEvent(messageEvent, content);

        if (input == null)
            return;

        CommandEvent event = new CommandEvent(commandler, messageEvent, msg, content);

        if (!commandler.getRootAlises().contains(event.getModule().toLowerCase()) || !parseCommand(event))
            return;

        AbstractMetaCommand metaCommand = event.getMetaCommand();

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
                event.reply(builder.buildMessage(event, message));
        } catch (IllegalAccessException | InvocationTargetException ex) {
            messageEvent.getChannel().sendMessage("Sorry! Something went wrong and I was unable to perform that commands.").queue();
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

    private boolean parseCommand(CommandEvent event) {
        return getMetaModule(event);
    }

    /**
     * Obtain the module the user intended, if no module can be found
     * fail silently as the user may not have intended to use out bot at all.
     * If a module is found, go to find the command as the next step via
     * {@link #getMetaCommand(MetaModule, CommandEvent)}. <br>
     * These are stored using the {@link CommandEvent} setter methods.
     *
     * @param event The message event that the user triggered.
     * @return If the command is still valid.
     */

    private boolean getMetaModule(CommandEvent event) {
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
                        event.getParams().add(0, Collections.singletonList(command));

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

    private boolean getMetaCommand(MetaModule metaModule, CommandEvent event) {
        String command = event.getCommand();

        if (command != null) {
            MetaCommand metaCommand = metaModule.getCommand(command);

            if (metaCommand != null) {
                event.setMetaCommand(metaCommand);

                if (event.getParams().size() != metaCommand.getInputRequired()) {
                    for (MetaOverload metaOverload : metaCommand.getOverloads()) {
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
            event.getParams().add(0, Collections.singletonList(command));

        if (event.getParams().size() != defaultCommand.getInputRequired()) {
            for (MetaOverload metaOverload : defaultCommand.getOverloads()) {
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

    public Builder getBuilder() {
        return builder;
    }

    public Validator getValidator() {
        return validator;
    }
}

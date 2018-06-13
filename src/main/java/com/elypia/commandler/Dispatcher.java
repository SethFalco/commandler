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

import java.lang.reflect.*;
import java.time.OffsetDateTime;
import java.util.*;

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

        MessageChannel channel = messageEvent.getChannel();
        MessageEvent event = new MessageEvent(commandler, messageEvent, msg, content);

        if (!event.isValid())
            return;

        CommandHandler handler = null;
        Collection<Method> commands = new ArrayList<>();

        for (CommandHandler h : commandler.getHandlers()) {
            MetaModule m = h.getModule();

            if (Arrays.asList(m.getModule().aliases()).contains(event.getModule())) {
                handler = h;
                break;
            }

            for (MetaCommand c : m.getCommands()) {
                if (c.isStatic()) {
                    if (Arrays.asList(c.getCommand().aliases()).contains(event.getModule())) {
                        handler = h;
                        commands.add(c.getMethod());
                        break;
                    }
                }
            }
        }



        if (handler == null) {
            for (CommandHandler h : commandler.getHandlers()) {
                MetaModule m = h.getModule();


            }
        } else {
            commands = CommandUtils.getCommands(event, handler);
        }

        if (commands.isEmpty()) {
            if (handler == null)
                return;

            channel.sendMessage("Sorry, that command doesn't exist, try help?").queue();
            return;
        }

        Method method = CommandUtils.getByParamCount(event, commands);

        if (method == null) {
            channel.sendMessage("You didn't give the correct number of parameters for that command, perhaps try help instead?").queue();
            return;
        }

        try {
            Object[] params = parseParameters(event, method);
            validator.validate(event, method, params);

            try {
                Object message = method.invoke(handler, params);

                if (message != null) {
                    sender.sendAsMessage(event, message, o -> {
                        Reaction[] reactions = method.getAnnotationsByType(Reaction.class);

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

    /**
     * Take the String parameters from the message event and parse them into the required
     * format the command method required to execute.
     *
     * @param event The message event to take parameters from.
     * @param method The method to imitate the fields of.
     * @return An Object[] array of all parameters parsed as required for the given method.
     * @throws IllegalArgumentException If one of the arguments could not be parsed in the required format.
     */

    private Object[] parseParameters(MessageEvent event, Method method) throws IllegalArgumentException {
        MetaCommand meta = MetaCommand.of(commandler, method); // Command data
        List<MetaParam> params = meta.getParams(); // Parameter data
        List<Object> inputs = event.getParams(); // User input parameters
        Object[] objects = new Object[params.size()]; // Parsed parameters to perform command

        int offset = 0;

        for (int i = 0; i < params.size(); i++) {
            MetaParam param = params.get(i);
            Class<?> type = param.getParameter().getType();

            if (type == MessageEvent.class) {
                objects[i] = event;
                offset++;
                continue;
            }

            Object input = inputs.get(i - offset);
            objects[i] = parser.parseParam(event, param, input);
        }

        return objects;
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

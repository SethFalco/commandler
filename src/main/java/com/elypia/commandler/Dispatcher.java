package com.elypia.commandler;

import com.elypia.commandler.events.MessageEvent;
import com.elypia.commandler.metadata.MetaCommand;
import com.elypia.commandler.metadata.MetaModule;
import com.elypia.commandler.metadata.MetaParam;
import com.elypia.commandler.parsing.Parser;
import com.elypia.commandler.validation.Validator;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class Dispatcher extends ListenerAdapter {

    private final Commandler commandler;
    private final Parser parser;

    public Dispatcher(final Commandler commandler) {
        this.commandler = commandler;
        this.parser = new Parser(commandler.getJDA());
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent messageEvent) {
        MessageChannel channel = messageEvent.getChannel();
        MessageEvent event = new MessageEvent(messageEvent, commandler.getPrefix());

        if (!event.isValid())
            return;

        CommandHandler handler = null;

        for (CommandHandler h : commandler.getHandlers()) {
            MetaModule m = MetaModule.of(h);

            if (Arrays.asList(m.getModule().aliases()).contains(event.getModule())) {
                handler = h;
                break;
            }
        }

        Collection<Method> commands = new ArrayList<>();

        if (handler == null) {
            for (CommandHandler h : commandler.getHandlers()) {
                MetaModule m = MetaModule.of(h);

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
        } else {
            commands = CommandUtils.getCommands(event, handler);
        }

        if (commands.isEmpty()) {
            if (handler == null)
                return;

            event.reply("Sorry, that command doesn't exist, try help?");
            return;
        }

        Method method = CommandUtils.getByParamCount(event, commands);

        if (method == null) {
            event.reply("Those parameters don't look right. DX Try help?");
            return;
        }

        try {
            Object[] params = parseParameters(event, method);
            Validator.validate(event, method, params);

            try {
                Object message = method.invoke(handler, params);

                if (message instanceof Message)
                    channel.sendMessage((Message)message).queue();

                else if (message instanceof MessageBuilder)
                    channel.sendMessage(((MessageBuilder)message).build()).queue();

                else if (message instanceof EmbedBuilder)
                    channel.sendMessage(((EmbedBuilder)message).build()).queue();

                else
                    channel.sendMessage(message.toString()).queue();

            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                ex.printStackTrace();
                event.reply("Sorry! Something went wrong and I was unable to perform that commands.");
            }
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            event.reply(ex.getMessage());
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

    public Object[] parseParameters(MessageEvent event, Method method) throws IllegalArgumentException {
        MetaCommand meta = MetaCommand.of(method); // Command data
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
}

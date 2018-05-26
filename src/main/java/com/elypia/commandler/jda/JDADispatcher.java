package com.elypia.commandler.jda;

import com.elypia.commandler.CommandHandler;
import com.elypia.commandler.CommandUtils;
import com.elypia.commandler.jda.events.MessageEvent;
import com.elypia.commandler.jda.parsing.JDAParamParser;
import com.elypia.commandler.metadata.MetaCommand;
import com.elypia.commandler.metadata.MetaModule;
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

public class JDADispatcher extends ListenerAdapter {

    private final JDACommandler commandler;
    private final JDAParamParser parser;

    public JDADispatcher(final JDACommandler commandler) {
        this.commandler = commandler;
        this.parser = new JDAParamParser(commandler.getJDA());
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
            Object[] params = CommandUtils.parseParameters(parser, event, method);
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
                event.reply("Sorry! Something went wrong and I was unable to perform that commands.");
            }
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            event.reply(ex.getMessage());
        }
    }
}

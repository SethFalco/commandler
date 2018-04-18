package com.elypia.commandler.jda;

import com.elypia.commandler.*;
import com.elypia.commandler.events.MessageEvent;
import com.elypia.commandler.metadata.MetaModule;
import com.elypia.commandler.validation.Validator;
import net.dv8tion.jda.core.*;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.lang.reflect.*;
import java.util.*;

public class JDADispatcher extends ListenerAdapter {

    private final JDACommandler commandler;

    public JDADispatcher(final JDACommandler commandler) {
        this.commandler = commandler;
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

        if (handler == null)
            return;

        Collection<Method> commands = CommandUtils.getCommands(event, handler);

        if (commands.isEmpty()) {
            event.reply("Sorry, that command doesn't exist, try help?");
            return;
        }

        Method method = CommandUtils.getByParamCount(event, commands);

        if (method == null) {
            event.reply("Those parameters don't look right. DX Try help?");
            return;
        }

        try {
            Object[] params = CommandUtils.parseParameters(event, method);
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

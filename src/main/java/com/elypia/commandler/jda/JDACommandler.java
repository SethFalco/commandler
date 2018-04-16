package com.elypia.commandler.jda;

import com.elypia.alexis.utils.BotUtils;
import com.elypia.alexis.utils.CommandUtils;
import com.elypia.commandler.CommandHandler;
import com.elypia.commandler.Validator;
import com.elypia.commandler.annotations.command.Module;
import com.elypia.commandler.events.MessageEvent;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.logging.Level;

/**
 * Received and performs all message or reactions
 * that refer to commands for this chatbot.
 */

public class JDACommandler extends ListenerAdapter {

    private JDA jda;

    /**
     * All registered handlers for this instance of the chatbot. <br>
     * The key is an array of aliases that refer to a module. <br>
     * The value is the module itself.
     */

    private Map<Collection<String>, CommandHandler> handlers;

    public JDACommandler(JDA jda) {
        this.jda = jda;
        handlers = new HashMap<>();
    }

    /**
     * Register multiple handlers.
     *
     * @param handlers
     */

    public void registerModules(CommandHandler... handlers) {
        for (CommandHandler handler : handlers)
            registerModule(handler);
    }

    public void registerModule(CommandHandler handler) {
        Module module = handler.getClass().getAnnotation(Module.class);

        if (module == null)
            throw new IllegalArgumentException("CommandHandler doesn't contain Module annotation!");

        Collection<String> aliases = new ArrayList<>();

        for (String alias : module.aliases())
            aliases.add(alias.toLowerCase());

        for (Collection<String> key : handlers.keySet()) {
            if (!Collections.disjoint(key, aliases))
                throw new IllegalArgumentException("CommandHandler contains alias which is already registered.");
        }

        handlers.put(aliases, handler);
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        MessageEvent event = new MessageEvent(chatbot, messageReceivedEvent);

        if (!event.isValid())
            return;

        CommandHandler handler = getHandler(event);

        if (handler == null)
            return;

        if (event.getCommand() == null)
            event.setCommand(handler);

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
                method.invoke(handler, params);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                event.reply("Sorry! Something went wrong and I was unable to perform that commands.");
                BotUtils.LOGGER.log(Level.SEVERE, "Failed to execute command!", ex);
            }
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            event.reply(ex.getMessage());
        }
    }

    private CommandHandler getHandler(MessageEvent event) {
        String module = event.getModule();

        for (Collection<String> key : handlers.keySet()) {
            if (key.contains(module))
                return handlers.get(key);
        }

        return null;
    }
}

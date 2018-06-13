package com.elypia.commandler.events;

import com.elypia.commandler.Commandler;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.confiler.Confiler;
import com.elypia.commandler.sending.Sender;
import net.dv8tion.jda.core.*;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.guild.GenericGuildEvent;
import net.dv8tion.jda.core.events.message.*;

import java.util.*;
import java.util.function.Consumer;
import java.util.regex.*;

public class MessageEvent {

    /**
     * The {@link MessageReceivedEvent} or {@link MessageUpdateEvent} as received from {@link JDA}.
     */

	private GenericMessageEvent event;

    /**
     * The {@link Message} that triggered this command. <br>
     * The contents of this message it not necessarily the command the message event is parsing.
     *
     * @see #trigger(String)
     */

	private Message message;

    /**
     * The parent {@link Commandler} instance that spawned this {@link MessageEvent}.
     */

	private Commandler commandler;

    /**
     * A reference to the configuration object for our {@link #confiler}.
     */

	private Confiler confiler;

    /**
     * If the command performed is considered a valid command at all.
     */

	private boolean isValid;

    /**
     * The root alias refers to the first segment of a command which may be the alias of a {@link Module} <br>
     * <strong>Possible null</strong>: If command is not {@link #isValid() valid}.
     */

	private String module;

    /**
     * The command segment of this command if valid, this refers to which command
     * in the specified module to perform. <br>
     * <br>
     * <strong>Possible null</strong>: <br>
     * If command is not {@link #isValid() valid}.
     */

	private String command;

    /**
     * A list of all parameters associated with this command. This list may contain
     * both {@link String}s and {@link String[]}s. List parameters (comma seperated) are stored
     * as an array.
     */

	private List<Object> params;

    /**
     * Calls {@link #MessageEvent(Commandler, GenericMessageEvent, Message, String)}
     * with the content as this the content of this message.
     *
     * @param commandler The parent {@link Commandler} instance spawning this event.
     * @param event The {@link MessageReceivedEvent} or {@link MessageUpdateEvent} as provided by {@link JDA}.
     * @param message The {@link Message} to attempt to process as a command.
     */

	public MessageEvent(Commandler commandler, GenericMessageEvent event, Message message) {
		this(commandler, event, message, message.getContentRaw());
	}

    /**
     *  Parse the {@link JDA} {@link GenericMessageEvent} into an event object {@link Commandler}
     *  will better utilise.
     *
     * @param commandler The parent {@link Commandler} instance spawning this event.
     * @param event The {@link MessageReceivedEvent} or {@link MessageUpdateEvent} as provided by {@link JDA}.
     * @param message The {@link Message} to attempt to process as a command.
     * @param content The command itself, this may be different from the contents of the message sent.
     */

	public MessageEvent(Commandler commandler, GenericMessageEvent event, Message message, String content) {
		this.commandler = commandler;
		this.event = event;
		this.message = message;

		confiler = commandler.getConfiler();
		params = new ArrayList<>();

		Pattern pattern = confiler.getCommandRegex(event);
		Matcher matcher = pattern.matcher(content);

		isValid = matcher.matches();

		if (!isValid)
			return;

		module = matcher.group("module");
		command = matcher.group("command");

		String parameters = matcher.group("params");

		if (parameters != null) {
			matcher = confiler.getParamRegex(event).matcher(parameters);

			while (matcher.find()) {
				String quotes = matcher.group("quotes");

				if (quotes != null) {
                    params.add(quotes);
                    continue;
                }

                String args = matcher.group("args");

				if (args != null) {
					String[] array = args.split("\\s*,\\s*");
					params.add(array.length == 1 ? array[0] : array);
				}
			}
		}
	}

    /**
     * Send a response to the {@link MessageChannel} this event occured in. <br>
     * This is useful when using {@link Consumer}s as you're unable to return from within
     * an anonymous function.
     *
     * @param message The item to send to the {@link Sender} to process.
     * @param <T> Type of object to send, this type is compared to the types in our
     * {@link Sender} so we know how to send it.
     */

	public <T> void reply(T message) {
		commandler.getDispatcher().getSender().sendAsMessage(this, message, null);
	}

    /**
     * If the bot has permission to, delete the message that triggered this event. <br>
     * This will do nothing in {@link ChannelType#PRIVATE} channels a user may only delete it's
     * own messages and the bot can't trigger it's own event.
     *
     * @return If the bot sent a request to delete the {@link Message}.
     */

	public boolean tryDeleteMessage() {
		if (!event.isFromType(ChannelType.TEXT))
			return false;

		if (event.getGuild().getSelfMember().hasPermission(event.getTextChannel(), Permission.MESSAGE_MANAGE)) {
            message.delete().queue();
            return true;
        }

		return false;
	}

    /**
     * Trigger another command which branches from this command. The new command
     * will use the same {@link GenericGuildEvent} and {@link Message} but with the command
     * specified instead. <br>
     * <strong>Do not</strong> include the prefix when executing a trigger. <br>
     * Example: <br>
     * event.trigger("bot help");
     *
     * @param trigger The new command to process instead.
     */

	public void trigger(String trigger) {
		String command = event.getJDA().getSelfUser().getAsMention();
		command += trigger;

		commandler.getDispatcher().process(event, message, command);
	}

    public GenericMessageEvent getMessageEvent() {
        return event;
    }

    public Message getMessage() {
        return message;
    }

    /**
     * @return If message sent actually fits the command format as
     * dictated in {@link Confiler#getCommandRegex(GenericMessageEvent)}.
     */

	public boolean isValid() {
		return isValid;
	}

    /**
     * @return Get the module of a command, this is normally the first thing
     * after the prefix.
     */

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
	    this.module = module;
    }

    /**
     * @return Get the command for this event.
     */

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
        this.command = command;
    }

	public List<Object> getParams() {
		return params;
	}
}

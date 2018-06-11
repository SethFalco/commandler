package com.elypia.commandler.events;

import com.elypia.commandler.Commandler;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.confiler.Confiler;
import net.dv8tion.jda.core.*;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.*;

import java.util.*;
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
     * The root alias of this command if valid, the root alias refers to
     * the first segment of a command which may be the alias of a {@link Module}
     * or {@link Static} {@link Command}. <br>
     * <br>
     * <strong>Possible null</strong>: If command is not {@link #isValid() valid}.
     */

	private String alias;

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

		alias = matcher.group("alias");
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

	public <T extends Object> void reply(T message) {
		commandler.getDispatcher().getSender().sendAsMessage(this, message, null);
	}

	public void tryDeleteMessage() {
		if (!event.isFromType(ChannelType.TEXT))
			return;

		if (event.getGuild().getSelfMember().hasPermission(event.getTextChannel(), Permission.MESSAGE_MANAGE))
			message.delete().queue();
	}

	public void trigger(String trigger) {
		String command = event.getJDA().getSelfUser().getAsMention();
		command += trigger;

		commandler.getDispatcher().process(event, message, command);
	}

	public boolean isValid() {
		return isValid;
	}

	public String getAlias() {
		return alias;
	}

	public String getCommand() {
		return command;
	}

	public List<Object> getParams() {
		return params;
	}

	public GenericMessageEvent getMessageEvent() {
		return event;
	}

	public Message getMessage() {
		return message;
	}
}

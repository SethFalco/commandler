package com.elypia.commandler.events;

import com.elypia.commandler.Commandler;
import com.elypia.commandler.confiler.Confiler;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.GenericMessageEvent;

import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.*;

public class MessageEvent {

	private GenericMessageEvent event;
	private Message message;

	private Commandler commandler;
	private Confiler confiler;
	private Method method;

	private boolean isValid;
	private String alias;
	private String command;
	private List<Object> params;

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
				String args = matcher.group("args");

				if (quotes != null)
					params.add(quotes);

				else if (args != null) {
					String[] array = args.split("\\s*,\\s*");

					if (array.length == 1)
						params.add(array[0]);
					else
						params.add(array);
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

	public Method getMethod() {
		return method;
	}

	public GenericMessageEvent getMessageEvent() {
		return event;
	}

	public Message getMessage() {
		return message;
	}
}

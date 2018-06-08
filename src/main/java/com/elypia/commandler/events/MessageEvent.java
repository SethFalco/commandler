package com.elypia.commandler.events;

import com.elypia.commandler.Commandler;
import com.elypia.commandler.annotations.Command;
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
	private Command annotation;

	private boolean isValid;
	private String module;
	private String submodule;
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

		module = matcher.group("module").toLowerCase();
		submodule = matcher.group("submodule");

		if (submodule != null)
			submodule = submodule.toLowerCase();

		command = matcher.group("command");

		if (command != null)
			command = command.toLowerCase();

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
		if (event.isFromType(ChannelType.TEXT)) {
			if (event.getGuild().getSelfMember().hasPermission(event.getTextChannel(), Permission.MESSAGE_MANAGE))
				message.delete().queue();
		} else {
			if (message.getAuthor() == event.getJDA().getSelfUser())
				message.delete().queue();
		}
	}

	public void trigger(String command) {
		commandler.getDispatcher().process(event, message, command);
	}

	public boolean isValid() {
		return isValid;
	}

	public String getModule() {
		return module;
	}

	public String getSubmodule() {
		return submodule;
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

	public Command getCommandAnnotation() {
		return annotation;
	}

	public void setMethod(Method method) {
		this.method = method;
		this.annotation = method.getAnnotation(Command.class);
	}

	public GenericMessageEvent getMessageEvent() {
		return event;
	}

	public Message getMessage() {
		return message;
	}
}

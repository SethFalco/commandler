package com.elypia.commandler.events;

import com.elypia.commandler.annotations.*;
import net.dv8tion.jda.core.*;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;
import java.lang.reflect.Method;
import java.util.*;
import java.util.List;
import java.util.function.Consumer;
import java.util.regex.*;

public class MessageEvent {

	private static final String COMMAND_REGEX = "(?i)^(?:<@!?%s>|%s)\\s{0,2}(?<module>[A-Z]+)(?:\\.(?<submodule>[A-Z]+))?(?: (?<command>.+?))?(?: (?<params>.*))?";
	private static final Pattern PARAM_PATTERN = Pattern.compile("(?<quotes>\\b(?<=\")(?:\\\\\"|[^\"])*(?=\"))|(?<args>[^\\s\",]+(?:\\s*,\\s*[^\\s\",]+)*)");

	private MessageReceivedEvent event;

	private boolean isValid;
	private String module;
	private String submodule;
	private String command;
	private List<Object> params;

	private Method method;
	private Command annotation;

	private Message reply;

	public MessageEvent(MessageReceivedEvent event, String prefix) {
		this(event, prefix, event.getMessage().getContentRaw());
	}

	public MessageEvent(MessageReceivedEvent event, String prefix, String content) {
		this.event = event;
		params = new ArrayList<>();

		String id = event.getJDA().getSelfUser().getId();
		String regex = String.format(COMMAND_REGEX, id, prefix);

		Pattern pattern = Pattern.compile(regex);
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

		// Due to change.
		if (parameters != null) {
			matcher = PARAM_PATTERN.matcher(parameters);

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

	public void reply(Object body) {
		event.getChannel().sendMessage(body.toString()).queue(this::afterReply);
	}

	public void reply(EmbedBuilder builder) {
		reply(builder, null);
	}

	public void reply(EmbedBuilder builder, Consumer<Message> consumer) {
		Guild guild = event.getGuild();

		if (guild != null) {
			Color color = guild.getSelfMember().getColor();
			builder.setColor(color);
		}

		event.getChannel().sendMessage(builder.build()).queue(msg -> {
			if (consumer != null)
				consumer.accept(msg);

			afterReply(msg);
		});
	}

	private void afterReply(Message message) {
//		reply = message;
//
//		Reaction[] reactions = method.getAnnotationsByType(Reaction.class);
//
//		for (Reaction reaction : reactions)
//			message.addReaction(reaction.alias()).queue();
	}

	public void tryDeleteMessage() {
		if (canDeleteMessage())
			event.getMessage().delete().queue();
	}

	public boolean canDeleteMessage() {
		if (event.getChannel().getType() == ChannelType.TEXT)
			return event.getGuild().getSelfMember().hasPermission(event.getTextChannel(), Permission.MESSAGE_MANAGE);

		return event.getAuthor() == event.getJDA().getSelfUser();
	}

	// Getters and setters

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

	public Message getReply() {
		return reply;
	}

	public MessageReceivedEvent getMessageEvent() {
		return event;
	}
}

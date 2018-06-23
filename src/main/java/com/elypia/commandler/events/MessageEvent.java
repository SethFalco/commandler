package com.elypia.commandler.events;

import com.elypia.commandler.Commandler;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.confiler.Confiler;
import com.elypia.commandler.metadata.*;
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
     * The {@link Message} that triggered this commands. <br>
     * The contents of this message it not necessarily the commands the message event is parsing.
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
     * If the commands performed is considered a valid commands at all.
     */

	private boolean isValid;

    /**
     * The root alias refers to the first segment of a commands which may be the alias of a {@link Module} <br>
     * <strong>Possible null</strong>: If commands is not {@link #isValid() valid}.
     */

	private String module;

	/**
	 * The module associated with this command if the module is valid.
	 */

	private MetaModule metaModule;

    /**
     * The commands segment of this commands if valid, this refers to which commands
     * in the specified module to perform. <br>
     * <br>
     * <strong>Possible null</strong>: <br>
     * If commands is not {@link #isValid() valid}.
     */

	private String command;

	/**
	 * The command associated with this command if the command is valid.
	 */

	private MetaCommand metaCommand;

    /**
     * A list of all parameters associated with this commands. This list may contain
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
     * @param message The {@link Message} to attempt to process as a commands.
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
     * @param message The {@link Message} to attempt to process as a commands.
     * @param content The commands itself, this may be different from the contents of the message sent.
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
		command = matcher.group("commands");

		String parameters = matcher.group("params");

		if (parameters != null) {
			Pattern splitPattern = confiler.getSplitRegex(event);
			matcher = confiler.getParamRegex(event).matcher(parameters);

			while (matcher.find()) {
				String group = matcher.group();
				Matcher splitMatcher = splitPattern.matcher(group);
				List<String> input = new ArrayList<>();

				while (splitMatcher.find()) {
					String quote = splitMatcher.group("quote");

					if (quote != null)
						input.add(quote);
					else
						input.add(splitMatcher.group("word"));
				}

				params.add(input.size() == 1 ? input.get(0) : input.toArray(new String[0]));
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
     * Trigger another commands which branches from this commands. The new commands
     * will use the same {@link GenericGuildEvent} and {@link Message} but with the commands
     * specified instead. <br>
     * <strong>Do not</strong> include the prefix when executing a trigger. <br>
     * Example: <br>
     * <em>event.trigger("bot help");</em>
     *
     * @param trigger The new commands to process instead.
     */

	public void trigger(String trigger) {
		String command = event.getJDA().getSelfUser().getAsMention();
		command += trigger;

		commandler.getDispatcher().process(event, message, command);
	}

	/**
	 * If an error occurs during the command, if for whatever reason
	 * the syntax is valid but the command event can't be performed,
	 * invalidate the command with the reason why we can't perform it.
	 * If the value is null, fail silently and don't send a message. <br>
	 * The reason for this is sent in the channel the command was performed in
	 * if not null.
	 *
	 * @param reason The message to post as to why we aren't going to perform the command.
	 * @return The new state of {@link #isValid}; this should <strong>always</strong> be <em>false</em>.
	 */

	public boolean invalidate(String reason) {
		isValid = false;

		if (reason != null)
			event.getChannel().sendMessage(reason).queue();

		return isValid;
	}

    /**
     * @return The {@link GenericMessageEvent} that caused this event.
     */

    public GenericMessageEvent getMessageEvent() {
        return event;
    }

    /**
     * @return The {@link Message} that caused this event.
     */

    public Message getMessage() {
        return message;
    }

    /**
     * @return If message sent actually fits the commands format as
     * dictated in {@link Confiler#getCommandRegex(GenericMessageEvent)}.
     */

	public boolean isValid() {
		return isValid;
	}

    /**
     * @return Get the module of a commands, this is normally the first thing
     * after the prefix.
     */

	public String getModule() {
		return module;
	}

	public void setMetaModule(MetaModule metaModule) {
		this.metaModule = metaModule;
		module = metaModule.getModule().aliases()[0];
	}

	public MetaModule getMetaModule() {
		return metaModule;
	}

    /**
     * @return Get the commands for this event.
     */

	public String getCommand() {
		return command;
	}

	public MetaCommand getMetaCommand() {
		return metaCommand;
	}

	public void setMetaCommand(MetaCommand metaCommand) {
		this.metaCommand = metaCommand;
		command = metaCommand.getCommand().aliases()[0];
	}

    /**
     * @return Get a list of all parameters.
     */

	public List<Object> getParams() {
		return params;
	}
}

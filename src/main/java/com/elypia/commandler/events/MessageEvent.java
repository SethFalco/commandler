package com.elypia.commandler.events;

import com.elypia.commandler.Commandler;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.confiler.Confiler;
import com.elypia.commandler.metadata.MetaCommand;
import com.elypia.commandler.modules.CommandHandler;
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
     * The commands segment of this commands if valid, this refers to which commands
     * in the specified module to perform. <br>
     * <br>
     * <strong>Possible null</strong>: <br>
     * If commands is not {@link #isValid() valid}.
     */

	private String command;

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

    /**
     * If the user performs a static commands, the regular expression should match
     * however it will not have the components that make up a commands grouped correctly.
     * We use {@link #setModule(String)} to correct the module.
     *
     * @param module The module we're setting this commands for.
     */

	public void setModule(String module) {
	    this.module = module;
    }

    public void setModule(CommandHandler handler) {
		setModule(handler.getModule().getModule().aliases()[0]);
	}

    /**
     * @return Get the commands for this event.
     */

	public String getCommand() {
		return command;
	}

    /**
     * If the user performs a default commands, the regular expression should match
     * however it will not have the components that make up a commands grouped correctly.
     * We use {@link #setCommand(String)} to correct the commands.
     *
     * @param command The commands we're setting this commands for.
     */

	public void setCommand(String command) {
        this.command = command;
    }

	public void setCommand(MetaCommand command) {
		setCommand(command.getCommand().aliases()[0]);
	}

    /**
     * @return Get a list of all parameters.
     */

	public List<Object> getParams() {
		return params;
	}
}

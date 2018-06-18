package com.elypia.commandlerbot.modules;

import com.elypia.commandler.modules.CommandHandler;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.annotations.filter.Search;
import com.elypia.commandler.annotations.validation.command.Scope;
import com.elypia.commandler.events.MessageEvent;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.*;

import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.elypia.commandler.data.SearchScope.LOCAL;

@Module(name = "Users", aliases = {"user", "users", "member", "members"}, description = "Get information, associations or stats on users!")
public class UserModule extends CommandHandler {

	@Overload("info")
	public EmbedBuilder getInfo(MessageEvent event) {
		return getInfo(event, event.getMessage().getAuthor());
	}

	@Scope(ChannelType.TEXT)
	@Default
	@Overload("info")
	@Command(name = "User Info", aliases = {"info", "information"}, help = "Obtain information on a user!")
	@Param(name = "user", help = "The user to display information for.")
	public EmbedBuilder getInfo(MessageEvent event, @Search(LOCAL) User user) {
		EmbedBuilder builder = new EmbedBuilder();
		String avatar = user.getEffectiveAvatarUrl();
		DateTimeFormatter format = DateTimeFormatter.ISO_LOCAL_DATE;
		Guild guild = event.getMessageEvent().getGuild();

		if (guild != null) {
			Member member = guild.getMember(user);

			builder.setAuthor(member.getEffectiveName());
			builder.addField("Online Status", member.getOnlineStatus().toString(), true);
			builder.addField("Status", member.getGame().getName(), true);
			builder.addField("Joined " + guild.getName(), member.getJoinDate().format(format), true);

			Collection<Role> roles = member.getRoles();

			if (!roles.isEmpty()) {
				StringJoiner joiner = new StringJoiner(", ");
				member.getRoles().forEach(o -> joiner.add(o.getName()));
				builder.addField("Roles", joiner.toString(), false);
			}
		} else {
			builder.setAuthor(user.getName());
		}

		builder.setThumbnail(avatar);
		builder.addField("Joined Discord", user.getCreationTime().format(format), true);

		if (user.isBot())
			builder.addField("Bot", "[Invite link!](" + String.format(BotModule.BOT_URL, user.getIdLong()), false);

		builder.setFooter("ID: " + user.getId(), null);

		return builder;
	}
}

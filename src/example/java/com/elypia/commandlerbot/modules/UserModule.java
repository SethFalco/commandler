package com.elypia.commandlerbot.modules;

import com.elypia.commandler.annotations.Command;
import com.elypia.commandler.annotations.CommandGroup;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.Param;
import com.elypia.commandler.jda.JDACommandHandler;
import com.elypia.commandler.jda.annotations.access.Scope;
import com.elypia.commandler.jda.annotations.filter.Search;
import com.elypia.commandler.jda.events.MessageEvent;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.*;

import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.StringJoiner;

import static com.elypia.commandler.jda.data.SearchScope.LOCAL;

@Module(
	name = "User (Member)",
	aliases = "user",
	description = "Get information or stats on global users!"
)
public class UserModule extends JDACommandHandler {

	@CommandGroup("info")
	public void getInfo(MessageEvent event) {
		getInfo(event, event.getMessageEvent().getAuthor());
	}

	@CommandGroup("info")
	@Command(aliases = "info", help = "Get some basic information on the user!")
	@Param(name = "user", help = "The user to display information for.")
	@Scope(ChannelType.TEXT)
	public void getInfo(MessageEvent event, @Search(LOCAL) User user) {
		EmbedBuilder builder = new EmbedBuilder();
		String avatar = user.getEffectiveAvatarUrl();
		DateTimeFormatter format = DateTimeFormatter.ISO_LOCAL_DATE;
		Guild guild = event.getMessageEvent().getGuild();
		Member member = guild.getMember(user);

		if (member != null) {
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

		event.reply(builder);
	}
}

package com.elypia.commandlerbot.modules;

import com.elypia.commandler.annotations.*;
import com.elypia.commandler.annotations.validation.command.NSFW;
import com.elypia.commandler.events.MessageEvent;
import com.elypia.commandler.modules.CommandHandler;
import net.dv8tion.jda.core.EmbedBuilder;
import okhttp3.*;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Module(name = "Misc.", aliases = {"util", "utils", "misc", "static"}, description = "Misc. commands that don't fit into another module.")
public class UtilModule extends CommandHandler {

	private OkHttpClient client;
	private Request nekoRequest;
	private List<String> nekoCache;

	public UtilModule() {
		client = new OkHttpClient();
		nekoRequest = new Request.Builder().url("https://nekos.life/api/neko").build();
		nekoCache = new ArrayList<>();
	}

	@Static
	@Command(name = "Character Count", aliases = "count", help = "Count the number of characters sent.")
	@Param(name = "text", help = "The text to count from.")
	public String count(String input) {
		return String.format("There are %,d characters in the input text.", input.length());
	}

	@Static
	@NSFW
	@Command(name = "Pet Neko", aliases = "neko", help = "Get a pet neko sent to you over Discord.")
	public void neko(MessageEvent event) {
		client.newCall(nekoRequest).enqueue(new Callback() {

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				JSONObject object = new JSONObject(response.body().string());

				EmbedBuilder builder = new EmbedBuilder();
				String image = object.getString("neko");
				builder.setImage(image);
				event.reply(builder);

				if (!nekoCache.contains(image))
					nekoCache.add(image);
			}

			@Override
			public void onFailure(Call call, IOException e) {
				if (nekoCache.size() == 0) {
					event.reply("We're all out of nekos. :C Maybe try again later?");
					return;
				}

				EmbedBuilder builder = new EmbedBuilder();
				builder.setImage(nekoCache.get(ThreadLocalRandom.current().nextInt(nekoCache.size())));
				event.reply(builder);
			}
		});
	}
}

package com.elypia.commandler.modules;

import com.elypia.commandler.*;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.confiler.Confiler;
import com.elypia.commandler.events.MessageEvent;
import com.elypia.commandler.metadata.MetaModule;
import net.dv8tion.jda.core.EmbedBuilder;

import java.util.*;

@Module(name = "Help", aliases = "help", description = "Get help and information on the bot and it's core features.")
public class HelpModule extends CommandHandler {

    private final static String STATIC = "Static commands are commands that can be performed without specifying the module it belongs too.\nFor example, if a command is `static` one could do: `!ping` instead of `!bot ping`. The `ping` command still belongs to the `bot` module regardless.";
    private final static String DEFAULT = "Default commands are commands modules can default too if we your input after doesn't match the alias of any existing commands in the module.\nFor example, if a command is `default` one could do: `!bot` instead of `!bot info`, we're still executing the `info` command in the `bot` module.";

    private Commandler commandler;

    public HelpModule(Commandler commandler) {
        this.commandler = commandler;
    }

    @Override
    @Default
    @CommandGroup("help")
    @Command(name = "Help", aliases = "help")
    public Object help(MessageEvent event) {
        return super.help(event);
    }

    @CommandGroup("help")
    @Param(name = "input", help = "The name of the module you'd like to query for help.")
    public void help(MessageEvent event, String input) {
        Confiler confiler = commandler.getConfiler();
        String prefix = confiler.getPrefix(event.getMessageEvent());
        String command = prefix + input + " help";
        event.trigger(command);
    }

    @Command(name = "List all Modules", aliases = "modules", help = "Learn about the various modules and what functionality they bring to Discord.")
    public EmbedBuilder modules(MessageEvent event) {
        Confiler confiler = commandler.getConfiler();
        String prefix = confiler.getPrefix(event.getMessageEvent());
        Collection<CommandHandler> handlers = commandler.getHandlers();
        EmbedBuilder builder = new EmbedBuilder();

        builder.setTitle("Help and other useful information!");
        builder.setDescription("The prefix in this channel is: `" + prefix + "`");

        boolean disabled = false;

        Iterator<CommandHandler> it = handlers.iterator();

        while (it.hasNext()) {
            CommandHandler handler = it.next();
            MetaModule meta = handler.getModule();
            Module module = meta.getModule();
            String name = module.name();
            String[] aliases = module.aliases();
            String description = module.description();

            if (!handler.isEnabled()) {
                name = "~~" + name + "~~";

                if (!disabled) {
                    disabled = true;
                    builder.appendDescription("\nCrossed out modules are disabled due to live issues.");
                }
            }

            StringJoiner joiner = new StringJoiner(", ");

            for (String alias : aliases)
                joiner.add("`" + alias + "`");

            description = "**Aliases: **" + joiner.toString() + "\n" + description;

            if (it.hasNext())
                description += "\n_ _";

            builder.addField(name, description, false);
        }

        builder.appendDescription("\n_ _");
        builder.setFooter("Try \"" + prefix + "{module} help\" for how to perform commands!", null);

        return builder;
    }

    @Command(name = "Explain Static Commands", aliases = "static", help = "Learn what a `static` command does and how to use them.")
    public String staticHelp() {
        return STATIC;
    }

    @Command(name = "Example Default Commands", aliases = "default", help = "Learn what a `default` command does and how to use them.")
    public String defaultHelp() {
        return DEFAULT;
    }
}

package com.elypia.commandler.jda;

import com.elypia.commandler.CommandHandler;
import com.elypia.commandler.annotations.Command;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.metadata.MetaModule;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;

import java.util.StringJoiner;

public class JDACommandHandler extends CommandHandler {

    protected JDA jda;

    @Override
    @Command(aliases = "help", help = "Displays all help information for commands in the module.")
    public Object help() {
        MetaModule meta = MetaModule.of(this);
        Module module = meta.getModule();

        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle(module.name());

        StringJoiner joiner = new StringJoiner(", ");

        for (String string : module.aliases())
            joiner.add("`" + string + "`");

        String description = "**Aliases**: " + joiner.toString() + "\n" + module.description();
        builder.setDescription(description);

        meta.getCommands().forEach(metaCommand -> {
            Command command = metaCommand.getCommand();

            if (!command.help().isEmpty())
                builder.addField(command.aliases()[0], command.help(), false);
        });

        return builder;
    }

    public JDA getJDA() {
        return jda;
    }

    public void setJDA(JDA jda) {
        this.jda = jda;
    }
}

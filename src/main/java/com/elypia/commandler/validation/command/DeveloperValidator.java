package com.elypia.commandler.validation.command;

import com.elypia.commandler.annotations.validation.command.Developer;
import com.elypia.commandler.events.CommandEvent;
import com.elypia.commandler.validation.ICommandValidator;
import net.dv8tion.jda.core.entities.User;

public class DeveloperValidator implements ICommandValidator<Developer> {

    @Override
    public boolean validate(CommandEvent event, Developer annotation) {
        User user = event.getMessage().getAuthor();

        for (long id : event.getConfiler().getDevelopers()) {
            if (id == user.getIdLong())
                return true;
        }

        return event.invalidate("Only the developers of the bot are allowed to perform this command.");
    }

    @Override
    public String help(Developer annotation) {
        return "Only the developers of the bot can perform this command.";
    }
}

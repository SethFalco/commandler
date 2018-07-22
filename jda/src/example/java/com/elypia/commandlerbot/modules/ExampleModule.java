package com.elypia.commandlerbot.modules;

import com.elypia.commandler.JDAHandler;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.validation.command.*;
import com.elypia.commandler.annotations.validation.param.*;
import com.elypia.commandler.parsers.DurationParser;
import net.dv8tion.jda.core.entities.*;

import java.time.Duration;
import java.util.stream.IntStream;

/**
 * Unlike with commands we hide them by simply not specifying {@link Command#help()},
 * with modules, we hide them by speciying {@link Module#hidden()} and setting it to true.
 * This is because unlike commands, when a command is hidden, there is no way to query it,
 * however, modules, you can still query the help if you know the module exists.
 */

@Module(name = "Example Module for Demo", aliases = {"example", "ex"})
public class ExampleModule extends JDAHandler {

    /**
     * Commandler provides ample inline validation through annotations.
     * This reduces total code on the front and a unified approach to handling that
     * particular type of validation, so no matter where you're checking length, you'll
     * return a similar message. <br>
     *
     * This uses the {@link Length} validator which allows you to specify the min and max
     * length a String parameter can be.
     */

    @Command(name = "Validate Length of Input", aliases = "length")
    @Param(name = "input", help = "Some random text to ensure  it's the right length.")
    public String length(@Length(min = 1, max = 32) String input) {
        return "Well done, the text was between 1 and 32 characters.";
    }

    /**
     * The {@link Option} inline validation allows you to limit what the
     * String can be to a fixed set of values, in this case we're specifying
     * a type of account, so the method will only ever execute if the parameter
     * type is "user", "bot", or "all".
     */

    @Command(name = "Validate Input is an Option", aliases = "option")
    @Param(name = "type", help = "A potential option from the list.")
    public String option(@Option({"user", "bot", "all"}) String type) {
        return "Well done, what you typed was a type of account.";
    }

    /**
     * This introduces a new type Commandler is compatible with by default.
     * Duration is a time value object which can store a duration or period of time
     * in units up to days, the user can specify a duration through means such as: <br>
     * <strong>!ex reminder "Hello, world!" in 1h</strong> or <strong>!ex reminder Hello every "24 hours"</strong> <br>
     * <br>
     * The {@link Period} annotation can be used to specify a min or max number of seconds
     * this duration can be, we wouldn't want people triggering / spamming a command every 5 seconds for example. <br>
     * <br>
     * You'll notice we gave your command an {@link Command#id()} here, we'll get onto that in the next method.
     */

    @Command(id = 2, name = "Reminders", aliases = "reminder", help = "Set a reminder to trigger.")
    @Param(name = "text", help = "What whould I remind you of?")
    @Param(name = "when", help = "Should we perform this once or repeat it? Specify with \"in\" or \"every\".")
    @Param(name = "time", help = "When to remind you!")
    public String reminder(@Everyone String text, @Option({"every", "in"}) String when, @Period(min = 3600) Duration time) {
        return reminder(null, text, when, time);
    }

    /**
     * This uses overloads, an overload is a child to a command and an alternative way of
     * performing it if we know they're trying to do this command, but specified
     * different parameters than the default. <br>
     * Overloads will use the command annotation from the command, the id specified
     * if the key for Commandler to understand the association between various methods.
     * The {@link Command} annotation is always copied as is, however parameters or validation
     * can be manipilated based on the values set in the overload. <br>
     * By default this will copy all parameters and validation over from it's parent command
     * then add params in this by default, however in this case we want to make sure user
     * is our first annotation so we specify the order. <br>
     */

    @Overload(value = 2, params = {"user", "text", "when", "time"})
    @Param(name = "user", help = "The user to remind.")
    public String reminder(User user, @Everyone String text, @Option({"every", "in"}) String when, @Period(min = 3600) Duration time) {
        String mention = user == null ? "you" : user.getAsMention();
        return String.format("Thanks! I will remind %s of that in this channel %s %s.", mention, text, DurationParser.forDisplay(time));
    }

    /**
     * This introduces command validation, command validation validates the entire command
     * rather than just a single parameter as it is used for a more global validation, or
     * to perform certain functionality. <br>
     * The {@link Secret} annotation will ensure that if the it is performed in a channel
     * with other users, delete the message ASAP (or request the user to) and PM
     * the user that they shouldn't be performing the command in public. <br>
     *
     * This is different from just using the {@link Scope} validation to specify {@link ChannelType}s
     * as that will only prevent the command from executing, but not protect the user if they
     * accidently perform a sensitive command in public.
     */

    @Secret
    @Command(name = "Tell me Password Pl0x", aliases = {"password", "pw"}, help = "Please tell me your password.")
    @Param(name = "password", help = "The password to your Discord account. :kappa:")
    public String password(String password) {
        return "Thank you!";
    }

    /**
     * By default, Commandler can handle normal parameters by word, or a parameter
     * with multiple words through quotes, for example: <br>
     * <strong>!bot say "Hello world!"</strong> - Valid
     * <strong>!bot say Hello</strong> - Valid
     * <strong>!bot say Hello world!</strong> - Invalid: this is two parameters <br>
     * <br>
     * As well as words, and quotes, Commandler can handle a list of items as a single parameter
     * by specifying an array as the method parameter. <br>
     * Normal parameters are seperated by spaces or quotes, however if there is a comma between
     * two parameters, they are considered items in a list paramter instead, for example: <br>
     * <strong>!ex total 100, 200, 300, 400</strong> <br>
     * This is <em>one</em> parameter. <br>
     * <strong>!ex total 100, 200, 300, 400 Hello</strong> Invalid: This is two parameters. <br>
     * The first parameter, being the list of numbers, and the second parameter being the word "Hello" as
     * it doesn't have a comma before it.
     */

    @Command(name = "List Parameters", aliases = "total", help = "Sum up all values provided.")
    @Param(name = "numbers", help = "A comma seperated list of all the numbers you want.")
    public int sum(int[] numbers) {
        return IntStream.of(numbers).sum();
    }

    /**
     * If a message returns an array, Commandler will send each
     * item as individual messages, as for why one would need this...
     * I have no idea... I just wanted to handle arrays somehow... :thinking:
     */

    @Command(name = "Spam the Chat", aliases = "spam", help = "Repeat a message multiple times.")
    @Param(name = "input", help = "The text to repeat.")
    @Param(name = "times", help = "The number of times to repeat this message.")
    public String[] spam(@Everyone String input, int times) {
        String[] array = new String[times];

        for (int i = 0; i < times; i++)
            array[i] = input;

        return array;
    }
}

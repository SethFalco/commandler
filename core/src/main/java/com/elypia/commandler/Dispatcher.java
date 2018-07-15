package com.elypia.commandler;

import com.elypia.commandler.building.Builder;
import com.elypia.commandler.impl.*;
import com.elypia.commandler.parsing.Parser;
import com.elypia.commandler.validation.Validator;

import java.lang.annotation.Annotation;

public class Dispatcher<E, M> implements IDispatcher<E, M> {

    /**
     * The {@link Commandler} instance which is what gives access to all
     * registers to add modules, parsers, senders, and validators.
     */

    protected final Commandler<?, E, M> commandler;

    protected final IConfiler<E, M> confiler;

    /**
     * Parser is the registry that allows you to define how objects are
     * intepretted from the raw string provided in chat.
     */

    protected final Parser parser;

    /**
     * Builder is the register that allows you to define how objects are
     * sent. This allows Commandler to know how certain return types
     * need to be processed in order to send in chat.
     */

    protected final Builder<M> builder;

    /**
     * Validator is a registery that allows you to define custom annotations
     * and mark them above methods or parameters in order to perform validation.
     * This is useful to ensure a unifed approach is done and to reduce code
     * required to make commands.
     */

    protected final Validator validator;

    /**
     * The JDADispatcher is what recieved JDA events and where Commandler begins
     * to parse events and determine the module / command combo and execute the method. <br>
     * Note: This will only perform events registered to Commandler and not other
     * {@link ListenerAdapter}s that may be associated with {@link JDA}.
     *
     * @param commandler The parent Commandler instance.
     */

    public JDADispatcher() {
        this.commandler = commandler;

        confiler = commandler.getConfiler();
        parser = new Parser();
        builder = new Builder(commandler);
        validator = new Validator(commandler);
    }

    /**
     * You can register custom parsers, this allows Commandler to know
     * how to parse certain objects as method parameters for you.
     *
     * @param clazz The type of class this parser will parse.
     * @param parser The parser which implements {@link IParamParser}, this has the method
     * which will interpret the String as our class object, else invalidate the command.
     * @param <T> The type of class this parses.
     */

    public <T> void registerParser(Class<T> clazz, IParser<T> parser) {
        parser.registerParser(clazz, parser);
    }

    public <T> void registerBuilder(Class<T> clazz, IBuilder<?, M> sender) {
        dispatcher.getBuilder().registerBuilder(clazz, sender);
    }

    public <T extends Annotation> void registerValidator(Class<T> clazz, IParamValidator<?, T> validator) {
        dispatcher.getValidator().registerValidator(clazz, validator);
    }

    public <T extends Annotation> void registerValidator(Class<T> clazz, ICommandValidator<T> validator) {
        dispatcher.getValidator().registerValidator(clazz, validator);
    }

    public Parser getParser() {
        return parser;
    }

    public Builder getBuilder() {
        return builder;
    }

    public Validator getValidator() {
        return validator;
    }
}

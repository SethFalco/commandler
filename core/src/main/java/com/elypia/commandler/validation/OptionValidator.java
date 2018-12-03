package com.elypia.commandler.validation;

import com.elypia.commandler.CommandEvent;
import com.elypia.commandler.annotations.validation.param.Option;
import com.elypia.commandler.impl.IParamValidator;
import com.elypia.commandler.metadata.MetaParam;

import java.util.*;
import java.util.function.Function;

public class OptionValidator extends IParamValidator<CommandEvent, String, Option> {

    public static final Function<Option, String> DEFAULT_HELP = (option) -> {
        return "Input must be one of the following options: " + String.join(", ", option.value());
    };

    public OptionValidator(Function<Option, String> help) {
        super(help);

        if (this.help == null)
            this.help = DEFAULT_HELP;
    }

    public OptionValidator() {
        this(DEFAULT_HELP);
    }

    @Override
    public boolean validate(CommandEvent event, String string, Option option, MetaParam param) {
        String[] options = option.value();

        if (!Arrays.asList(options).contains(string)) {
            StringJoiner joiner = new StringJoiner(", ");

            for (String op : options)
                joiner.add("`" + op + "`");

            return false;
        }

        return true;
    }
}

package com.elypia.commandler.validation;

import com.elypia.commandler.annotations.validation.param.Option;
import com.elypia.commandler.CommandEvent;
import com.elypia.commandler.metadata.MetaParam;
import com.elypia.commandler.impl.IParamValidator;

import java.util.*;

public class OptionValidator implements IParamValidator<String, Option> {

    @Override
    public boolean validate(CommandEvent event, String string, Option option, MetaParam param) {
        String[] options = option.value();

        if (!Arrays.asList(options).contains(string)) {
            StringJoiner joiner = new StringJoiner(", ");

            for (String op : options)
                joiner.add("`" + op + "`");

            String format = "Sorry, the parameter `%s` must be within the following options: %s.";
            return false;
        }

        return true;
    }

    @Override
    public String help(Option option) {
        return "Input must be one of the following options: " + String.join(", ", option.value());
    }
}

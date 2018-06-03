package com.elypia.commandler.validation.param;

import com.elypia.commandler.annotations.Param;
import com.elypia.commandler.annotations.validation.param.*;
import com.elypia.commandler.metadata.MetaParam;
import com.elypia.commandler.validation.IParamValidator;

import java.util.*;

public class OptionValidator implements IParamValidator<String, Option> {

    @Override
    public void validate(String string, Option option, MetaParam param) throws IllegalAccessException {
        Param p = param.getParams();
        Collection<String> strings = Arrays.asList(option.value());

        if (!strings.contains(string)) {
            String format = "Sorry, the parameter `%s` must be within the following options:\n%s\n\n The documentation states:\n%s";
            String help = "`" + p.name() + ": " + p.help() + "`";
            String list = String.join(", ", strings);
            String message = String.format(format, p.name(), list, help);
            throw new IllegalAccessException(message);
        }
    }
}

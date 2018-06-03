package com.elypia.commandler.validation.param;

import com.elypia.commandler.annotations.Param;
import com.elypia.commandler.annotations.validation.param.Length;
import com.elypia.commandler.metadata.MetaParam;
import com.elypia.commandler.validation.IParamValidator;

public class LengthValidator implements IParamValidator<String, Length> {

    @Override
    public void validate(String s, Length length, MetaParam param) {
        Param p = param.getParams();
        int l = s.length();

        if (l < length.min() || l > length.max()) {
            String format = "Sorry, the paramter `%s` must be between %,d and %,d characters long!\n\nThe documentation states:\n%s";
            String help = "`" + p.name() + ": " + p.help() + "`";
            String message = String.format(format, p.name(), length.min(), length.max(), help);
            throw new IllegalArgumentException(message);
        }
    }
}

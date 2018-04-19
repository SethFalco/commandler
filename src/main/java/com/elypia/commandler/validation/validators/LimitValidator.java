package com.elypia.commandler.validation.validators;

import com.elypia.commandler.annotations.Param;
import com.elypia.commandler.metadata.MetaParam;
import com.elypia.commandler.validation.annotations.Limit;
import com.elypia.commandler.validation.impl.IParamValidator;

public class LimitValidator implements IParamValidator<Long, Limit> {

    @Override
    public void validate(Long value, Limit limit, MetaParam param) {
        Param p = param.getParams();

        if (value < limit.min() || value > limit.max()) {
            String format = "Sorry, the paramter `%s` must be between %,d and %,d!\n\nThe documentation states:\n%s";
            String help = "`" + p.name() + ": " + p.help() + "`";
            String message = String.format(format, p.name(), limit.min(), limit.max(), help);
            throw new IllegalArgumentException(message);
        }
    }
}

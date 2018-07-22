package com.elypia.commandler.validation;

import com.elypia.commandler.CommandEvent;
import com.elypia.commandler.annotations.validation.param.Limit;
import com.elypia.commandler.impl.IParamValidator;
import com.elypia.commandler.metadata.MetaParam;

public class LimitValidator implements IParamValidator<Number, Limit> {

    @Override
    public boolean validate(CommandEvent event, Number value, Limit limit, MetaParam param) {
        if (value.intValue() < limit.min() || value.intValue() > limit.max())
            return false;

        return true;
    }

    @Override
    public String help(Limit limit) {
        return buildMessage("This parameter", limit);
    }

    private String buildMessage(String item, Limit limit) {
        if (limit.min() == Long.MIN_VALUE && limit.max() != Long.MAX_VALUE)
            return String.format("%s must have a value less than or equal to %,d!", item, limit.max());
        else if (limit.min() != Long.MIN_VALUE && limit.max() == Long.MAX_VALUE)
            return String.format("%s must have a value greater than or equal to %,d!", item, limit.min());
        else
            return String.format("%s must have a value between %,d and %,d!", item, limit.min(), limit.max());
    }
}

package com.elypia.commandler.validation.param;

import com.elypia.commandler.annotations.validation.param.*;
import com.elypia.commandler.events.MessageEvent;
import com.elypia.commandler.metadata.MetaParam;
import com.elypia.commandler.validation.IParamValidator;

public class LimitValidator implements IParamValidator<Number, Limit> {

    @Override
    public boolean validate(MessageEvent event, Number value, Limit limit, MetaParam param) {
        if (value.intValue() < limit.min() || value.intValue() > limit.max())
            return event.invalidate(buildMessage("The parameter `" + param.getParamAnnotation().name(), limit));

        return true;
    }

    @Override
    public String help(Limit limit) {
        return buildMessage("This parameter", limit);
    }

    private String buildMessage(String item, Limit limit) {
        if (limit.min() == Long.MIN_VALUE && limit.max() != Long.MAX_VALUE)
            return String.format("%s must have a value less than %,d!", item, limit.max());
        else if (limit.min() != Long.MIN_VALUE && limit.max() == Long.MAX_VALUE)
            return String.format("%s must have a value greater than %,d!", item, limit.min());
        else
            return String.format("%s must have a value between %,d and %,d!", item, limit.min(), limit.max());
    }
}

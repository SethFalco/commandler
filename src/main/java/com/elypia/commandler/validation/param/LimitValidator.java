package com.elypia.commandler.validation.param;

import com.elypia.commandler.annotations.Param;
import com.elypia.commandler.annotations.validation.param.Limit;
import com.elypia.commandler.events.MessageEvent;
import com.elypia.commandler.metadata.MetaParam;
import com.elypia.commandler.validation.IParamValidator;

public class LimitValidator implements IParamValidator<Long, Limit> {

    @Override
    public boolean validate(MessageEvent event, Long value, Limit limit, MetaParam param) {
        Param p = param.getParamAnnotation();

        if (value < limit.min() || value > limit.max()) {
            String format = "Sorry, the paramter `%s` must be between %,d and %,d!\n\nThe documentation states:\n%s";
            String help = "`" + p.name() + ": " + p.help() + "`";
            String message = String.format(format, p.name(), limit.min(), limit.max(), help);
            return event.invalidate(message);
        }

        return true;
    }

    @Override
    public String help(Limit limit) {
        return String.format("This parameter must be between the values %,d and %,d.", limit.min(), limit.max());
    }
}

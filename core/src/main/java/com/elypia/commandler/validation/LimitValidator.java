package com.elypia.commandler.validation;

import com.elypia.commandler.CommandEvent;
import com.elypia.commandler.annotations.validation.param.Limit;
import com.elypia.commandler.impl.IParamValidator;
import com.elypia.commandler.metadata.MetaParam;

import java.util.function.Function;

public class LimitValidator extends IParamValidator<CommandEvent, Number, Limit> {

    public static final Function<Limit, String> DEFAULT_HELP = (limit) -> {
        if (limit.min() == Long.MIN_VALUE && limit.max() != Long.MAX_VALUE)
            return String.format("Must have a value less than or equal to %,d!", limit.max());
        else if (limit.min() != Long.MIN_VALUE && limit.max() == Long.MAX_VALUE)
            return String.format("Must have a value greater than or equal to %,d!", limit.min());
        else
            return String.format("Must have a value between %,d and %,d!", limit.min(), limit.max());
    };

    public LimitValidator(Function<Limit, String> help) {
        super(help);

        if (this.help == null)
            this.help = DEFAULT_HELP;
    }

    public LimitValidator() {
        this(DEFAULT_HELP);
    }

    @Override
    public boolean validate(CommandEvent event, Number value, Limit limit, MetaParam param) {
        long l = value.longValue();
        return l >= limit.min() && l <= limit.max();
    }
}

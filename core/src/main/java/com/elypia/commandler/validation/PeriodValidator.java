package com.elypia.commandler.validation;

import com.elypia.commandler.CommandEvent;
import com.elypia.commandler.annotations.validation.param.Period;
import com.elypia.commandler.impl.IParamValidator;
import com.elypia.commandler.metadata.MetaParam;
import com.elypia.commandler.parsers.DurationParser;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class PeriodValidator extends IParamValidator<CommandEvent, Duration, Period> {

    public static final Function<Period, String> DEFAULT_HELP = (period) -> {
        Duration max = Duration.ofSeconds(period.max());
        if (period.min() == 0 && period.max() != Long.MAX_VALUE)
            return String.format("Must be shorter than %s!", DurationParser.forDisplay(max));

        Duration min = Duration.ofSeconds(period.min());
        if (period.min() != 0 && period.max() == Long.MAX_VALUE)
            return String.format("Must be longer than %s!", DurationParser.forDisplay(min));

        else
            return String.format("Must have be between %s and %s!", DurationParser.forDisplay(min), DurationParser.forDisplay(max));
    };

    public PeriodValidator(Function<Period, String> help) {
        super(help);

        if (this.help == null)
            this.help = DEFAULT_HELP;
    }

    public PeriodValidator() {
        this(DEFAULT_HELP);
    }

    @Override
    public boolean validate(CommandEvent event, Duration duration, Period period, MetaParam param) {
        TimeUnit unit = period.unit();
        long value = unit.convert(duration.getSeconds(), TimeUnit.SECONDS);
        return value >= period.min() && value <= period.max();
    }
}

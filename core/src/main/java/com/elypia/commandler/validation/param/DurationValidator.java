package com.elypia.commandler.validation.param;

import com.elypia.commandler.annotations.validation.param.Period;
import com.elypia.commandler.events.CommandEvent;
import com.elypia.commandler.metadata.MetaParam;
import com.elypia.commandler.parsing.parsers.DurationParser;
import com.elypia.commandler.impl.IParamValidator;

import java.time.Duration;

public class DurationValidator implements IParamValidator<Duration, Period> {

    @Override
    public boolean validate(CommandEvent event, Duration duration, Period period, MetaParam param) {
        long seconds = duration.getSeconds();

        if (seconds < period.min() || seconds> period.max())
            return event.invalidate(buildMessage("The parameter `" + param.getParamAnnotation().name() + "`", period));

        return true;
    }

    @Override
    public String help(Period period) {
        return buildMessage("This parameter", period);
    }

    private String buildMessage(String item, Period period) {
        Duration max = Duration.ofSeconds(period.max());
        if (period.min() == 0 && period.max() != Long.MAX_VALUE)
            return String.format("%s must be shorter than %s!", item, DurationParser.forDisplay(max));

        Duration min = Duration.ofSeconds(period.min());
        if (period.min() != 0 && period.max() == Long.MAX_VALUE)
            return String.format("%s must be longer than %s!", item, DurationParser.forDisplay(min));

        else
            return String.format("%s must have be between %s and %s!", item, DurationParser.forDisplay(min), DurationParser.forDisplay(max));
    }
}

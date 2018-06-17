package com.elypia.commandler.validation.param;

import com.elypia.commandler.annotations.Param;
import com.elypia.commandler.annotations.validation.param.Length;
import com.elypia.commandler.events.MessageEvent;
import com.elypia.commandler.metadata.MetaParam;
import com.elypia.commandler.validation.IParamValidator;

public class LengthValidator implements IParamValidator<String, Length> {

    @Override
    public boolean validate(MessageEvent event, String s, Length length, MetaParam param) {
        Param p = param.getParamAnnotation();
        int l = s.length();

        if (l < length.min() || l > length.max()) {
            String format = "Sorry, the paramter `%s` must be between %,d and %,d characters long!\n\nThe documentation states:\n%s";
            String help = "`" + p.name() + ": " + p.help() + "`";
            String message = String.format(format, p.name(), length.min(), length.max(), help);
            return event.invalidate(message);
        }

        return true;
    }

    @Override
    public String help(Length length) {
        return String.format("This parameter must be between %,d and %,d characters long.", length.min(), length.max());
    }
}

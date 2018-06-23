package com.elypia.commandler.validation.param;

import com.elypia.commandler.annotations.validation.param.Length;
import com.elypia.commandler.events.MessageEvent;
import com.elypia.commandler.metadata.MetaParam;
import com.elypia.commandler.validation.IParamValidator;

public class LengthValidator implements IParamValidator<String, Length> {

    @Override
    public boolean validate(MessageEvent event, String s, Length length, MetaParam param) {
        int l = s.length();

        if (l < length.min() || l > length.max())
            return event.invalidate(buildMessage("The parameter `" + param.getParamAnnotation().name() + "`", length));

        return true;
    }

    @Override
    public String help(Length length) {
        return buildMessage("This parameter", length);
    }

    private String buildMessage(String item, Length length) {
        if (length.min() == 0 && length.max() != Integer.MAX_VALUE)
            return String.format("%s must less than %,d characters long!", item, length.max());
        else if (length.min() != 0 && length.max() == Integer.MAX_VALUE)
            return String.format("%s must be more than %,d characters long!", item, length.min());
        else
            return String.format("%s must be between %,d and %,d characters long.", item, length.min(), length.max());
    }
}

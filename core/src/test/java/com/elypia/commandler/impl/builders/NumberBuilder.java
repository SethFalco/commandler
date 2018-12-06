package com.elypia.commandler.impl.builders;

import com.elypia.commandler.console.*;
import com.elypia.commandler.impl.*;
import com.elypia.commandler.test.impl.*;
import console.*;

public class NumberBuilder implements IStringBuilder<Number> {

    @Override
    public String build(StringCommand event, Number output) {
        return String.valueOf(output);
    }
}

package com.elypia.commandler.impl.builders;

import com.elypia.commandler.console.*;
import com.elypia.commandler.impl.*;
import com.elypia.commandler.test.impl.*;
import console.*;

public class DefaultBuilder implements IStringBuilder<String> {

    @Override
    public String build(StringCommand event, String output) {
        return output;
    }
}

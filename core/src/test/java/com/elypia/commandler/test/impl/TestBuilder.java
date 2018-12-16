package com.elypia.commandler.test.impl;

import com.elypia.commandler.impl.CommandEvent;
import com.elypia.commandler.interfaces.IBuilder;

public interface TestBuilder<O> extends IBuilder<CommandEvent<String, String>, O, String> {

}

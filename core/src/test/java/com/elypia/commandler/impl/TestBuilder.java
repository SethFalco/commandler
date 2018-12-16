package com.elypia.commandler.impl;

import com.elypia.commandler.interfaces.IBuilder;

public interface TestBuilder<O> extends IBuilder<CommandEvent<String, String>, O, String> {

}

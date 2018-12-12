package com.elypia.commandler.impl;

import com.elypia.commandler.interfaces.IBuilder;

public interface TestBuilder<O> extends IBuilder<CommandEvent<Void, String, String>, O, String> {

}

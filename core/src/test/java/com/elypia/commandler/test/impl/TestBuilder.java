package com.elypia.commandler.test.impl;

import com.elypia.commandler.AbstractCommandlerEvent;
import com.elypia.commandler.interfaces.Builder;

public interface TestBuilder<O> extends Builder<AbstractCommandlerEvent<String, String>,O, String> {

}

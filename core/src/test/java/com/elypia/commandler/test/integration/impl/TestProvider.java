package com.elypia.commandler.test.integration.impl;

import com.elypia.commandler.interfaces.Provider;

public interface TestProvider<O> extends Provider<AbstractCommandlerEvent<String, String>,O, String> {

}

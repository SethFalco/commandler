package com.elypia.commandler.impl;

import com.elypia.commandler.interfaces.IBuilder;

// ? This is just to save us from doing the <?, String> part each time
public interface IStringBuilder<I> extends IBuilder<StringCommand, I, String> {

}
